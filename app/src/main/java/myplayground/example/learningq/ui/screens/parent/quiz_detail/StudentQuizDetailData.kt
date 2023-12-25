package myplayground.example.learningq.ui.screens.parent.quiz_detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import myplayground.example.learningq.model.QuizQuestion

data class ParentStudentQuizDetailData(
    val questionList: List<QuizQuestion>? = null,
    val isLoadingQuestionList: Boolean = false,

    val currentQuestionIndex: Int = 0, // 0 = first question, 1 = second question , etc...
    val listEssayAnswer: SnapshotStateList<String> = mutableStateListOf(),
    val listSelectedMultipleChoice: SnapshotStateList<Int> = mutableStateListOf(), // 1 = A, 2 = B, 3 = C, etc...
)

sealed class ParentStudentQuizDetailEvent {
    data class FetchQuestions(val quizId: String) : ParentStudentQuizDetailEvent()
    data class EssayAnswerChanged(val essayAnswer: String) : ParentStudentQuizDetailEvent()
    data class SelectedMultipleChoiceChanged(val choice: Int) : ParentStudentQuizDetailEvent()

    object Init : ParentStudentQuizDetailEvent()
    object PrevQuestion : ParentStudentQuizDetailEvent()
    object NextQuestion : ParentStudentQuizDetailEvent()
    object Submit : ParentStudentQuizDetailEvent()

    sealed class ValidationEvent {
        class None() : ValidationEvent()
        class Success() : ValidationEvent()
        class Failure(val code: Int = 0, val msg: String) : ValidationEvent()
    }
}
