package myplayground.example.learningq.model

sealed class Role {
    object Student : Role()
    object Teacher : Role()
    object Admin : Role()
    object Parent : Role()

    override fun toString(): String {
        return when (this) {
            is Student -> "student"
            is Teacher -> "teacher"
            is Admin -> "admin"
            is Parent -> "parent"
        }
    }

    companion object {
        fun list() = listOf(
            Student,
            Teacher,
            Admin,
            Parent,
        )

        fun parseString(str: String): Role {
            return when (str) {
                "student" -> Student
                "teacher" -> Teacher
                "admin" -> Admin
                "parent" -> Parent

                else -> Student
            }
        }
    }
}
