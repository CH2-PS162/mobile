package myplayground.example.learningq.ui.screens.student.report_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import myplayground.example.learningq.model.Class
import myplayground.example.learningq.repository.Repository

class StudentReportDetailViewModel(
    private val repository: Repository,
) : ViewModel() {
    init {
        onEvent(StudentReportDetailEvent.Init)
    }

    fun onEvent(event: StudentReportDetailEvent) {
        viewModelScope.launch {
            when (event) {
                is StudentReportDetailEvent.Init -> {
                }
            }
        }
    }

}