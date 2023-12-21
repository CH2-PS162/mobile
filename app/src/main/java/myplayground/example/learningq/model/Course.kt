package myplayground.example.learningq.model

import myplayground.example.learningq.utils.CustomDayOfWeek
import myplayground.example.learningq.utils.TimeInSeconds


data class Course(
    val id: String = "",
    val classId: String = "",
    val name: String = "",
    val teacherUserId: String = "",
    val dayOfWeek: CustomDayOfWeek = CustomDayOfWeek.UNSPECIFIED, // 1 Monday, 7 Sunday
    val startTimeInMinutes: TimeInSeconds = TimeInSeconds(0), // 3600 = 01:00, 7200 = 02:00
    val endTimeInMinutes: TimeInSeconds = TimeInSeconds(0),
    val description: String? = null,
)

data class CourseStudent(
    val courseId: String = "",
    val studentUserId: String = "",
)