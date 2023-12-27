package myplayground.example.learningq.ui.screens.admin.user_add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import myplayground.example.learningq.repository.Repository
import retrofit2.HttpException

class AdminUserAddViewModel(
    private val repository: Repository,
) : ViewModel() {
    private val _uiState = mutableStateOf(AdminUserAddInputData())
    private val _isLoading = mutableStateOf(false)

    val uiState: State<AdminUserAddInputData> = _uiState
    val isLoading: State<Boolean> = _isLoading

    init {
        onEvent(AdminUserAddEvent.Init)
    }

    fun onEvent(event: AdminUserAddEvent) {
        viewModelScope.launch {
            when (event) {
                is AdminUserAddEvent.Init -> {
                }

                is AdminUserAddEvent.RoleChanged -> {
                    _uiState.value = _uiState.value.copy(role = event.role)
                }

                is AdminUserAddEvent.EmailChanged -> {
                    _uiState.value = _uiState.value.copy(email = event.email)
                }

                is AdminUserAddEvent.NameChanged -> {
                    _uiState.value = _uiState.value.copy(name = event.name)
                }

                is AdminUserAddEvent.PasswordChanged -> {
                    _uiState.value = _uiState.value.copy(password = event.password)
                }

                is AdminUserAddEvent.Submit -> {
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