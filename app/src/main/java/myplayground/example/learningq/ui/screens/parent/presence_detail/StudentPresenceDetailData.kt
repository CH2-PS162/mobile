package myplayground.example.learningq.ui.screens.parent.presence_detail

import myplayground.example.learningq.model.Course

data class ParentStudentPresenceDetailData(
    val processedCourseId: String? = null,
    val isLoading: Boolean = false,
)

sealed class ParentStudentPresenceDetailEvent {
    object Init : ParentStudentPresenceDetailEvent()
    data class FetchPresence(val classId: String) : ParentStudentPresenceDetailEvent()

    data class CoursePresenceSubmit(val course: Course) : ParentStudentPresenceDetailEvent()
}
