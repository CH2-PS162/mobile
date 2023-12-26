package myplayground.example.learningq.ui.screens.sign_in

import myplayground.example.learningq.model.Role

data class SignInInputData(
    val username: String = "",
    val password: String = "",

    val formError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
)

sealed class SignInUIEvent {
    data class UsernameChanged(val username: String) : SignInUIEvent()
    data class PasswordChanged(val password: String) : SignInUIEvent()

    object Submit : SignInUIEvent()

    sealed class ValidationEvent {
        class None() : ValidationEvent()
        class Failure(val code: Int = 0, val msg: String) : ValidationEvent()
        data class Success(val role: Role) : ValidationEvent()
    }
}