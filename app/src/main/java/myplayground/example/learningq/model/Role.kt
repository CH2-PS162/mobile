package myplayground.example.learningq.model

sealed class Role {
    object Student : Role()
    object Teacher : Role()
    object Admin : Role()
}
