package myplayground.example.learningq.model

data class User(
    val id: String,
    val email: String = "",
    val name: String,
    val role: Role,
    val password: String = "",
    val imageUrl: String? = null,

    val studentUserId: String? = null,
    val studentUser: User? = null,
)