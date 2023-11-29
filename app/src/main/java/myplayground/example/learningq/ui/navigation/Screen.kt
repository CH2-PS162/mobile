package myplayground.example.learningq.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object Landing : Screen("landing")
    object StudentDashboard : Screen("studentdashboard")
}