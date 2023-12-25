package myplayground.example.learningq.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object Landing : Screen("landing")
    object Setting : Screen("setting")

    object AuthLoading : Screen("authloading")


    object StudentDashboard : Screen("studentdashboard")
    object StudentProfile : Screen("studentprofile")
    object StudentQuiz : Screen("studentquiz")
    object StudentReport : Screen("studentreport")
    object StudentReportDetail : Screen("studentreportdetail")

    object StudentQuizDetail : Screen("studentquiz/{id}") {
        fun createRoute(id: String) = "studentquiz/$id"
    }

    object StudentPresence : Screen("studentpresence")
    object StudentPresenceDetail : Screen("studentpresence/{id}") {
        fun createRoute(classId: String) = "studentpresence/$classId"
    }

    object StudentFeedback : Screen("studentfeedback")

    object ParentDashboard : Screen("parentdashboard")
    object ParentProfile : Screen("parentprofile")
    object ParentQuiz : Screen("parentquiz")
    object ParentReport : Screen("parentreport")
    object ParentReportDetail : Screen("parentreportdetail")

    object ParentQuizDetail : Screen("parentquiz/{id}") {
        fun createRoute(id: String) = "parentquiz/$id"
    }

    object ParentPresence : Screen("parentpresence")
    object ParentPresenceDetail : Screen("parentpresence/{id}") {
        fun createRoute(classId: String) = "parentpresence/$classId"
    }

    object ParentFeedback : Screen("parentfeedback")

    object TeacherProfile : Screen("teacherprofile")
    object TeacherDashboard : Screen("teacherdashboard")
    object TeacherFeedback : Screen("teacherfeedback/{id}") {
        fun createRoute(courseId: String) = "teacherfeedback/$courseId"
    }

    object TeacherFeedbackDetail : Screen("teacherfeedbackdetail/{id}") {
        fun createRoute(feedbackId: String) = "teacherfeedbackdetail/$feedbackId"
    }

    object TeacherQuiz : Screen("teacherquiz")
    object TeacherQuizAdd : Screen("teacherquizadd")


    object AdminClass : Screen("adminclass")
    object AdminClassDetail : Screen("adminclassdetail/{id}") {
        fun createRoute(classId: String) = "adminclassdetail/$classId"
    }

    object AdminClassAdd : Screen("adminclassadd")
    object AdminCourse : Screen("admincourse")
    object AdminCourseDetail : Screen("admincoursedetail/{id}") {
        fun createRoute(courseId: String) = "admincoursedetail/$courseId"
    }

    object AdminCourseAdd : Screen("admincourseadd")
    object AdminUser : Screen("adminuser")
    object AdminUserAdd : Screen("adminuseradd")
    object AdminProfile : Screen("adminprofile")
}
