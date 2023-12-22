package myplayground.example.learningq.ui.screens.admin.class_add

data class AdminClassAddInputData(
    val name: String = "",
)

sealed class AdminClassAddEvent {
    object Init : AdminClassAddEvent()
    object Submit : AdminClassAddEvent()

    data class NameChanged(val name: String) : AdminClassAddEvent()
}