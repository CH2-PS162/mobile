package myplayground.example.learningq.ui.screens.parent.presence_detail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.LocalStorageManager
import myplayground.example.learningq.model.Course
import myplayground.example.learningq.repository.Repository

class ParentStudentPresenceDetailViewModel(
    private val repository: Repository,
    private val localStorageManager: LocalStorageManager,
) : ViewModel() {
    private val _courseState: MutableStateFlow<PagingData<Course>> =
        MutableStateFlow(PagingData.empty())
    private val _inputData = mutableStateOf(ParentStudentPresenceDetailData())

    val courseState: StateFlow<PagingData<Course>> = _courseState
    val inputData: State<ParentStudentPresenceDetailData> = _inputData

    init {
        onEvent(ParentStudentPresenceDetailEvent.Init)
    }

    fun onEvent(event: ParentStudentPresenceDetailEvent) {
        viewModelScope.launch {
            when (event) {
                is ParentStudentPresenceDetailEvent.Init -> {
                }

                is ParentStudentPresenceDetailEvent.FetchPresence -> {
                    repository.fetchStudentCourseByClassId(
                        event.classId,
                        Injection.provideApiService(localStorageManager = localStorageManager)
                    ).distinctUntilChanged().cachedIn(viewModelScope)
                        .collect {
                            _courseState.value = it
                        }
                }

                is ParentStudentPresenceDetailEvent.CoursePresenceSubmit -> {
                    _inputData.value = _inputData.value.copy(
                        isLoading = true,
                        processedCourseId = event.course.id,
                    )
                    delay(1500)

                    _inputData.value = _inputData.value.copy(
                        isLoading = false,
                        processedCourseId = null,
                    )
                }
            }
        }
    }

}