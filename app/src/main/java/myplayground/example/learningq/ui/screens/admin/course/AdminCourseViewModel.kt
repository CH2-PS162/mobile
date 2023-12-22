package myplayground.example.learningq.ui.screens.admin.course

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
import myplayground.example.learningq.model.Course
import myplayground.example.learningq.repository.Repository
import myplayground.example.learningq.ui.screens.student.report.StudentReportEvent

class AdminCourseViewModel(
    private val repository: Repository,
    private val localStorageManager: LocalStorageManager,
) :
    ViewModel() {
    private val _courseState: MutableStateFlow<PagingData<Course>> =
        MutableStateFlow(PagingData.empty())
    val courseState: StateFlow<PagingData<Course>> = _courseState

    init {
        onEvent(StudentReportEvent.Init)
    }

    fun onEvent(event: StudentReportEvent) {
        viewModelScope.launch {
            when (event) {
                is StudentReportEvent.Init -> {
                    repository.fetchAdminCoursePaging(
                        Injection.provideApiService(localStorageManager = localStorageManager)
                    ).distinctUntilChanged().cachedIn(viewModelScope)
                        .collect {
                            _courseState.value = it
                        }
                }
            }
        }
    }
}