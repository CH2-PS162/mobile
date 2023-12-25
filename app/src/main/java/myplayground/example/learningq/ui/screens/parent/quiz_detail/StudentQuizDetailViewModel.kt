package myplayground.example.learningq.ui.screens.parent.quiz_detail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.LocalStorageManager
import myplayground.example.learningq.repository.Repository

class ParentStudentQuizDetailViewModel(
    private val repository: Repository,
    private val localStorageManager: LocalStorageManager,
) : ViewModel() {
    private val _uiState = mutableStateOf(ParentStudentQuizDetailData())
    private val _isLoading = mutableStateOf(false)

    val uiState: State<ParentStudentQuizDetailData> = _uiState
    val isLoading: State<Boolean> = _isLoading


    fun onEvent(event: ParentStudentQuizDetailEvent) {
        when (event) {
            is ParentStudentQuizDetailEvent.Init -> {
            }

            is ParentStudentQuizDetailEvent.FetchQuestions -> {
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        isLoadingQuestionList = true,
                    )

                    val questions = repository.fetchStudentQuizQuestionByQuizId(
                        event.quizId,
                        Injection.provideApiService(localStorageManager),
                    )

                    _uiState.value = _uiState.value.copy(
                        questionList = questions,
                        isLoadingQuestionList = false,
                        listEssayAnswer = MutableList(questions.size) { "" }.toMutableStateList(),
                        listSelectedMultipleChoice = MutableList(questions.size) { 0 }.toMutableStateList(),
                    )
                }
            }

            is ParentStudentQuizDetailEvent.EssayAnswerChanged -> {
                val updatedListEssayAnswer = _uiState.value.listEssayAnswer
                updatedListEssayAnswer[_uiState.value.currentQuestionIndex] = event.essayAnswer
                _uiState.value = _uiState.value.copy(
                    listEssayAnswer = updatedListEssayAnswer,
                )
            }

            is ParentStudentQuizDetailEvent.SelectedMultipleChoiceChanged -> {
                val updatedListMultipleChoice = _uiState.value.listSelectedMultipleChoice
                updatedListMultipleChoice[_uiState.value.currentQuestionIndex] = event.choice
                _uiState.value = _uiState.value.copy(
                    listSelectedMultipleChoice = updatedListMultipleChoice,
                )
            }

            is ParentStudentQuizDetailEvent.Submit -> {

            }

            is ParentStudentQuizDetailEvent.NextQuestion -> {
                _uiState.value = _uiState.value.copy(
                    currentQuestionIndex = _uiState.value.currentQuestionIndex + 1,
                )
            }

            is ParentStudentQuizDetailEvent.PrevQuestion -> {
                _uiState.value = _uiState.value.copy(
                    currentQuestionIndex = _uiState.value.currentQuestionIndex - 1,
                )
            }

        }
    }
}