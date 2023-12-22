package myplayground.example.learningq.ui.screens.admin.class_add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import myplayground.example.learningq.repository.Repository
import retrofit2.HttpException

class AdminClassAddViewModel(
    private val repository: Repository,
) : ViewModel() {
    private val _uiState = mutableStateOf(AdminClassAddInputData())
    private val _isLoading = mutableStateOf(false)

    val uiState: State<AdminClassAddInputData> = _uiState
    val isLoading: State<Boolean> = _isLoading

    init {
        onEvent(AdminClassAddEvent.Init)
    }

    fun onEvent(event: AdminClassAddEvent) {
        viewModelScope.launch {
            when (event) {
                is AdminClassAddEvent.Init -> {
                }


                is AdminClassAddEvent.NameChanged -> {
                    _uiState.value = _uiState.value.copy(name = event.name)
                }


                is AdminClassAddEvent.Submit -> {
                    submitFeedback()
                }

            }
        }
    }

    private suspend fun submitFeedback() {
        _isLoading.value = true

        try {
            delay(1500)
        } catch (e: HttpException) {

        } finally {
            _isLoading.value = false
        }
    }
}