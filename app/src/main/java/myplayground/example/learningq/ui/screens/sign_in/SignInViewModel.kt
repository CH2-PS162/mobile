package myplayground.example.learningq.ui.screens.sign_in

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import org.tensorflow.lite.Interpreter
import retrofit2.HttpException

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
                        val role = Role.parseString(token.role)
                        localStorageManager.saveUserToken(token.auth_token ?: "")
                        localStorageManager.saveUserRole(role)

                        validationEvent.emit(SignInUIEvent.ValidationEvent.Success(role))
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
}

