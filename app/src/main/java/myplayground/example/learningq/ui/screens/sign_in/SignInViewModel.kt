package myplayground.example.learningq.ui.screens.sign_in

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.LocalStorageManager
import myplayground.example.learningq.model.Role
import myplayground.example.learningq.repository.Repository
import myplayground.example.learningq.repository.UserLoginInput
import myplayground.example.learningq.ui.utils.StringValidationRule
import myplayground.example.learningq.ui.utils.validate
import myplayground.example.learningq.utils.allNull
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import retrofit2.HttpException
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SignInViewModel(
    private val context: Context,
    private val repository: Repository,
    private val localStorageManager: LocalStorageManager,
) : ViewModel() {
    private val _uiState = mutableStateOf(SignInInputData())
    private val _isLoading = mutableStateOf(false)

    val uiState: State<SignInInputData> = _uiState
    val isLoading: State<Boolean> = _isLoading

    val validationEvent = MutableSharedFlow<SignInUIEvent.ValidationEvent>()

    var tfLiteModel: Interpreter? = null

    fun onEvent(event: SignInUIEvent) {
        when (event) {
            is SignInUIEvent.UsernameChanged -> {
                _uiState.value = _uiState.value.copy(username = event.username)
            }

            is SignInUIEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = event.password)
            }

            is SignInUIEvent.Submit -> {
                validateAndSubmit()
            }
        }
    }

    private fun validateAndSubmit() {
        val emailResultError = _uiState.value.username.validate(
            StringValidationRule.Required("Required field"),
            StringValidationRule.Email("Not a valid email"),
        ).toErrorMessage()

        val passwordResultError = _uiState.value.password.validate(
            StringValidationRule.Required("Required field")
        ).toErrorMessage()

        _uiState.value = _uiState.value.copy(usernameError = emailResultError)
        _uiState.value = _uiState.value.copy(passwordError = passwordResultError)

        val hasError = !listOf(
            emailResultError,
            passwordResultError,
        ).allNull()

        viewModelScope.launch {
            if (!hasError) {
                _isLoading.value = true

                try {
                    val token = repository.userLogin(
                        UserLoginInput(
                            email = _uiState.value.username,
                            password = _uiState.value.password,
                        ),
                        Injection.provideApiService(localStorageManager),
                    )

                    if (token?.auth_token != null && token.auth_token.isNotEmpty()) {
                        localStorageManager.saveUserToken(token.auth_token ?: "")
                        localStorageManager.saveUserRole(Role.parseString(token.role))

                        validationEvent.emit(SignInUIEvent.ValidationEvent.Success())
                    }
                } catch (e: HttpException) {
                    _uiState.value =
                        _uiState.value.copy(formError = e.response()?.errorBody()?.string())
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun predict(context: Context) {
        tfLiteModel = Interpreter(loadModelFile(context))

        val inputSentence =
            "Guru ini sangat pandai dalam mengajar dan membuat materi mudah dipahami"
        val inputSize = inputSentence.split(" ").size;

        val classifier = Classifier(context, "nlp_token.json", inputSize)
        classifier.processVocab(object : Classifier.VocabCallback {
            override fun onVocabProcessed() {
                val tokenizedMessage = classifier.tokenize(inputSentence)
                val paddedMessage = classifier.padSequence(tokenizedMessage)

//                val results = classifySequence(paddedMessage)

                tfLiteModel?.close()
            }
        })
    }

    private fun classifySequence(sequence: IntArray): FloatArray {
        val inputs: Array<FloatArray> = arrayOf(sequence.map { it.toFloat() }.toFloatArray())

        val outputs: Array<FloatArray> = arrayOf(FloatArray(1))
        tfLiteModel?.run(inputs, outputs)
        return outputs[0]
    }


    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("nlp.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declareLength = fileDescriptor.declaredLength

        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset,
            declareLength,
        )
    }

}


class Classifier(context: Context, jsonFilename: String, inputMaxLen: Int) {

    private var context: Context? = context

    private var filename: String = jsonFilename

    private var maxlen: Int = inputMaxLen

    private var vocabData: HashMap<String, Int>? = null

    private fun loadJSONFromAsset(filename: String): String? {
        var json: String?
        try {
            val inputStream = context!!.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun processVocab(callback: VocabCallback) {
        CoroutineScope(Dispatchers.Main).launch {
            loadVocab(callback, loadJSONFromAsset(filename)!!)
        }
    }

    fun tokenize(message: String): IntArray {
        val parts: List<String> = message.split(" ")
        val tokenizedMessage = ArrayList<Int>()
        for (part in parts) {
            if (part.trim() != "") {
                var index: Int? = 0
                index = if (vocabData!![part] == null) {
                    0
                } else {
                    vocabData!![part]
                }
                tokenizedMessage.add(index!!)
            }
        }
        return tokenizedMessage.toIntArray()
    }

    fun padSequence(sequence: IntArray): IntArray {
        val maxlen = this.maxlen
        if (sequence.size > maxlen) {
            return sequence.sliceArray(0..maxlen)
        } else if (sequence.size < maxlen) {
            val array = ArrayList<Int>()
            array.addAll(sequence.asList())
            for (i in array.size until maxlen) {
                array.add(0)
            }
            return array.toIntArray()
        } else {
            return sequence
        }
    }


    interface VocabCallback {
        fun onVocabProcessed()
    }

    private fun loadVocab(callback: VocabCallback, json: String) {
        with(Dispatchers.Default) {
            val jsonObject = JSONObject(json)
            val iterator: Iterator<String> = jsonObject.keys()
            val data = HashMap<String, Int>()
            while (iterator.hasNext()) {
                val key = iterator.next()
                data[key] = jsonObject.get(key) as Int
            }
            with(Dispatchers.Main) {
                vocabData = data
                callback.onVocabProcessed()
            }
        }
    }

}