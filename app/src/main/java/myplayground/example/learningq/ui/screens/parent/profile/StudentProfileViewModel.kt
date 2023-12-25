package myplayground.example.learningq.ui.screens.parent.profile

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
import myplayground.example.learningq.utils.AuthManager

class ParentStudentProfileViewModel(
    val authManager: AuthManager,
) :
    ViewModel() {
}