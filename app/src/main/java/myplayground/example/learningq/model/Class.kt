package myplayground.example.learningq.model

data class Class(
    val id: String = "",
    val courseId: String = "",
    val name: String = "",
    val instructorId: Int = 0,
    val schedule: String = "",
    val description: String = "",
)

data class StudentClass(
    val classId: String = "",
    val studentId: String = "",
)
