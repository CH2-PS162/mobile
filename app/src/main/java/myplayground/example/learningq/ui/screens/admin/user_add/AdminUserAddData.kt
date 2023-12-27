package myplayground.example.learningq.ui.screens.admin.user_add

import myplayground.example.learningq.model.Role

data class AdminUserAddInputData(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: Role? = null,
)

sealed class AdminUserAddEvent {
    object Init : AdminUserAddEvent()
    object Submit : AdminUserAddEvent()
    data class NameChanged(val name: String) : AdminUserAddEvent()
    data class EmailChanged(val email: String) : AdminUserAddEvent()
    data class PasswordChanged(val password: String) : AdminUserAddEvent()
    data class RoleChanged(val role: Role) : AdminUserAddEvent()
}