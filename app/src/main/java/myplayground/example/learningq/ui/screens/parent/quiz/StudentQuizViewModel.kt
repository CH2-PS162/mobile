package myplayground.example.learningq.ui.screens.parent.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.LocalStorageManager
import myplayground.example.learningq.model.Quiz
import myplayground.example.learningq.repository.Repository

class ParentStudentQuizViewModel(
    private val repository: Repository,
    private val localStorageManager: LocalStorageManager,
) : ViewModel() {
    private val _quizState: MutableStateFlow<PagingData<Quiz>> =
        MutableStateFlow(PagingData.empty())
    val quizState: StateFlow<PagingData<Quiz>> = _quizState

    init {
        onEvent(ParentStudentQuizEvent.Init)
    }

    fun onEvent(event: ParentStudentQuizEvent) {
        viewModelScope.launch {
            when (event) {
                is ParentStudentQuizEvent.Init -> {
                    repository.fetchStudentQuizPaging(
                        Injection.provideApiService(localStorageManager = localStorageManager),
                    )
                        .distinctUntilChanged()
                        .cachedIn(viewModelScope)
                        .collect {
                            _quizState.value = it
                        }
                }
            }
        }
    }
}