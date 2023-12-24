package myplayground.example.learningq.ui.screens.admin.course_add

import androidx.compose.runtime.snapshots.SnapshotStateList
import myplayground.example.learningq.model.Class
import myplayground.example.learningq.model.User
import myplayground.example.learningq.utils.CustomDayOfWeek
import myplayground.example.learningq.utils.TimeInSeconds

data class AdminCourseAddInputData(
    val classList: SnapshotStateList<Class>? = null,
    val isFetchClassLoading: Boolean = false,

    val teacherList: SnapshotStateList<User>? = null,
    val isFetchTeacherLoading: Boolean = false,

    val selectedClass: Class? = null,
    val selectedTeacher: User? = null,
    val name: String = "",
    val dayOfWeek: CustomDayOfWeek = CustomDayOfWeek.UNSPECIFIED,
    val startTimeInSeconds: TimeInSeconds = TimeInSeconds(0),
    val endTimeInSeconds: TimeInSeconds = TimeInSeconds(0),
)

sealed class AdminCourseAddEvent {
    object Init : AdminCourseAddEvent()
    object Submit : AdminCourseAddEvent()

    data class ClassChanged(val `class`: Class) : AdminCourseAddEvent()
    data class TeacherChanged(val teacher: User) : AdminCourseAddEvent()
    data class NameChanged(val name: String) : AdminCourseAddEvent()
    data class DayOfWeekChanged(val dayOfWeek: CustomDayOfWeek) : AdminCourseAddEvent()
    data class startTimeInSecondChanged(val startTimeInSeconds: TimeInSeconds) :
        AdminCourseAddEvent()

    data class endTimeInSecondChanged(val endTimeInSeconds: TimeInSeconds) : AdminCourseAddEvent()
}