package myplayground.example.learningq.ui.screens.sign_in

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import myplayground.example.learningq.repository.Repository
import myplayground.example.learningq.repository.UserLoginInput
import myplayground.example.learningq.utils.allTrue

class SignInViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = mutableStateOf(SignInInputData())
    val uiState: State<SignInInputData> = _uiState

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
        val usernameResultError = _uiState.value.username.isEmpty()
        val passwordResultError = _uiState.value.password.isEmpty()

        _uiState.value = _uiState.value.copy(hasUsernameError =  usernameResultError)
        _uiState.value = _uiState.value.copy(hasPasswordError =  passwordResultError)

        val hasError = listOf(
            usernameResultError,
            passwordResultError,
        ).allTrue()

        viewModelScope.launch {
            if (!hasError) {
                repository.userLogin(
                    UserLoginInput(
                        username = _uiState.value.username,
                        password = _uiState.value.password,
                    )
                ).collect { token ->
                    validationEvent.emit(SignInUIEvent.ValidationEvent.Success(token))
                }

            } else {
                validationEvent.emit(SignInUIEvent.ValidationEvent.Failure(0, "Validation Failed"))
            }
        }
    }
}