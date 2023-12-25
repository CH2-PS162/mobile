package myplayground.example.learningq.ui.screens.parent.feedback

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import myplayground.example.learningq.repository.Repository
import retrofit2.HttpException

class ParentStudentFeedbackViewModel(
    private val repository: Repository,
) : ViewModel() {
    private val _uiState = mutableStateOf(ParentStudentFeedbackInputData())
    private val _isLoading = mutableStateOf(false)

    val uiState: State<ParentStudentFeedbackInputData> = _uiState
    val isLoading: State<Boolean> = _isLoading

    init {
        onEvent(ParentStudentFeedbackEvent.Init)
    }

    fun onEvent(event: ParentStudentFeedbackEvent) {
        viewModelScope.launch {
            when (event) {
                is ParentStudentFeedbackEvent.Init -> {
                }

                is ParentStudentFeedbackEvent.FeedbackAnswerChanged -> {
                    _uiState.value = _uiState.value.copy(feedbackAnswer = event.feedback)
                }

                is ParentStudentFeedbackEvent.Submit -> {
                    submitFeedback()
                }
            }
        }
    }

    private suspend fun submitFeedback() {
        _isLoading.value = true

        try {
            delay(1500)
        }catch(e: HttpException) {

        } finally {
            _isLoading.value = false
        }
    }
}