package myplayground.example.learningq.ui.screens.student.dashboard

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
import myplayground.example.learningq.model.Class
import myplayground.example.learningq.repository.Repository

class StudentDashboardViewModel(
    private val repository: Repository,
    private val localStorageManager: LocalStorageManager,
) :
    ViewModel() {
    private val _classState: MutableStateFlow<PagingData<Class>> =
        MutableStateFlow(PagingData.empty())
    val classState: StateFlow<PagingData<Class>> = _classState

    init {
        onEvent(StudentDashboardEvent.Init)
    }

    fun onEvent(event: StudentDashboardEvent) {
        viewModelScope.launch {
            when (event) {
                is StudentDashboardEvent.Init -> {
                    repository.fetchStudentClassPaging(
                        Injection.provideApiService(localStorageManager= localStorageManager),
                    )
                        .distinctUntilChanged()
                        .cachedIn(viewModelScope)
                        .collect {
                            _classState.value = it
                        }
                }
            }
        }
    }

}