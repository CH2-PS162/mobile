package myplayground.example.learningq.ui.screens.parent.feedback

data class ParentStudentFeedbackInputData(
    val feedbackAnswer: String = "",
)

sealed class ParentStudentFeedbackEvent {
    object Init : ParentStudentFeedbackEvent()
    object Submit: ParentStudentFeedbackEvent()

    data class FeedbackAnswerChanged(val feedback: String): ParentStudentFeedbackEvent()
}