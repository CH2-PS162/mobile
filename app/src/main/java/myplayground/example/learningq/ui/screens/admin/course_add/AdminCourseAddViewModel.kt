package myplayground.example.learningq.ui.screens.admin.course_add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.LocalStorageManager
import myplayground.example.learningq.repository.Repository
import retrofit2.HttpException

class AdminCourseAddViewModel(
    private val repository: Repository,
    private val localStorageManager: LocalStorageManager,
) : ViewModel() {
    private val _uiState = mutableStateOf(AdminCourseAddInputData())
    private val _isLoading = mutableStateOf(false)

    val uiState: State<AdminCourseAddInputData> = _uiState
    val isLoading: State<Boolean> = _isLoading

    init {
        onEvent(AdminCourseAddEvent.Init)
    }

    fun onEvent(event: AdminCourseAddEvent) {
        when (event) {
            is AdminCourseAddEvent.Init -> {
                viewModelScope.launch {
                    val classes = repository.fetchAdminClass(
                        Injection.provideApiService(localStorageManager = localStorageManager)
                    )

                    _uiState.value = _uiState.value.copy(
                        classList = classes.toMutableStateList(),
                    )
                }
                viewModelScope.launch {
                    val teacher = repository.fetchAdminUserTeacher(
                        Injection.provideApiService(localStorageManager = localStorageManager)
                    )

                    _uiState.value = _uiState.value.copy(
                        teacherList = teacher.toMutableStateList(),
                    )
                }
            }

            is AdminCourseAddEvent.ClassChanged -> {
                _uiState.value = _uiState.value.copy(
                    selectedClass = event.`class`
                )
            }

            is AdminCourseAddEvent.DayOfWeekChanged -> {
                _uiState.value = _uiState.value.copy(
                    dayOfWeek = event.dayOfWeek
                )
            }

            is AdminCourseAddEvent.TeacherChanged -> {
                _uiState.value = _uiState.value.copy(
                    selectedTeacher = event.teacher,
                )
            }

            is AdminCourseAddEvent.endTimeInSecondChanged -> {

            }

            is AdminCourseAddEvent.startTimeInSecondChanged -> {

            }


            is AdminCourseAddEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(name = event.name)
            }

            is AdminCourseAddEvent.Submit -> {
                viewModelScope.launch {
                    submitFeedback()
                }
            }
        }
    }

    private suspend fun submitFeedback() {
        _isLoading.value = true

        try {
            delay(1500)
        } catch (e: HttpException) {

        } finally {
            _isLoading.value = false
        }
    }
}