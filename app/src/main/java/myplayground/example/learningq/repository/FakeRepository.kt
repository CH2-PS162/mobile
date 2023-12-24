package myplayground.example.learningq.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import myplayground.example.learningq.model.Class
import myplayground.example.learningq.model.Course
import myplayground.example.learningq.model.Feedback
import myplayground.example.learningq.model.Quiz
import myplayground.example.learningq.model.QuizQuestion
import myplayground.example.learningq.model.Token
import myplayground.example.learningq.model.User
import myplayground.example.learningq.network.ApiService
import myplayground.example.learningq.network.request.LoginRequest
import myplayground.example.learningq.repository.paging.AdminClassPagingSource
import myplayground.example.learningq.repository.paging.AdminCoursePagingSource
import myplayground.example.learningq.repository.paging.StudentClassPagingSource
import myplayground.example.learningq.repository.paging.StudentCoursePagingSource
import myplayground.example.learningq.repository.paging.StudentQuizPagingSource
import myplayground.example.learningq.repository.paging.TeacherCoursePagingSource
import myplayground.example.learningq.repository.paging.TeacherQuizPagingSource
import myplayground.example.learningq.repository.paging.UserPagingSource

class FakeRepository(
    context: Context,
) : Repository {

    override suspend fun userLogin(request: UserLoginInput, apiService: ApiService): Token? {
        delay(1500)

        val response = apiService.login(
            LoginRequest(
                email = request.email,
                password = request.password,
            )
        )

        return Token(
            auth_token = response.accessToken,
            role = response.role,
        )
    }

    override fun userRegister(request: UserRegisterInput): Flow<Token?> {
        return flow {
            emit(null)
        }
    }

    override suspend fun userMe(token: String, apiService: ApiService): User? {
        delay(1500)

        return apiService.userMe().user
    }

    override suspend fun fetchStudentQuizPaging(apiService: ApiService): Flow<PagingData<Quiz>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                StudentQuizPagingSource(
                    apiService,
                )
            }).flow
    }

    override suspend fun fetchStudentQuizQuestionByQuizId(
        quizId: String,
        apiService: ApiService
    ): List<QuizQuestion> {
        return apiService.fetchStudentQuizQuestion(quizId)
    }


    override suspend fun fetchStudentClassPaging(apiService: ApiService): Flow<PagingData<Class>> {
        return Pager(
            config = PagingConfig(
                pageSize = 7,
                initialLoadSize = 7,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                StudentClassPagingSource(
                    apiService,
                )
            }).flow
    }

    override suspend fun fetchStudentCourseByClassId(
        classId: String,
        apiService: ApiService
    ): Flow<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 7,
                initialLoadSize = 7,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                StudentCoursePagingSource(
                    apiService,
                    classId,
                )
            }).flow
    }

    override suspend fun fetchTeacherQuizPaging(apiService: ApiService): Flow<PagingData<Quiz>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                TeacherQuizPagingSource(
                    apiService,
                )
            }).flow
    }

    override suspend fun fetchTeacherClassByTeacherUserId(
        teacherUserId: String,
        apiService: ApiService
    ): List<Class> {
        return apiService.fetchTeacherClassByTeacherUserId(
            teacherUserId
        )
    }

    override suspend fun fetchTeacherFeedbackByTeacherUserIdAndCourseId(
        teacherUserId: String,
        courseId: String,
        apiService: ApiService
    ): List<Feedback> {
        return apiService.fetchTeacherFeedbackByTeacherUserIdAndCourseId(teacherUserId, courseId)
    }

    override suspend fun fetchFeedbackById(
        feedbackId: String,
        apiService: ApiService,
    ): Feedback? {
        return apiService.fetchFeedbackById(feedbackId)
    }

    override suspend fun fetchTeacherCoursePaging(
        teacherId: String,
        apiService: ApiService
    ): Flow<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                TeacherCoursePagingSource(
                    teacherId,
                    apiService,
                )
            }).flow
    }

    override suspend fun fetchAdminClassPaging(
        apiService: ApiService,
    ): Flow<PagingData<Class>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                AdminClassPagingSource(
                    apiService,
                )
            }).flow
    }

    override suspend fun fetchAdminClass(apiService: ApiService): List<Class> {
        return apiService.fetchAdminClass()
    }

    override suspend fun fetchAdminUserTeacher(apiService: ApiService): List<User> {
        return apiService.fetchAdminUserTeacher()
    }

    override suspend fun fetchAdminCoursePaging(apiService: ApiService): Flow<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                AdminCoursePagingSource(
                    apiService,
                )
            }).flow
    }

    override suspend fun fetchUserPaging(apiService: ApiService): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                UserPagingSource(
                    apiService,
                )
            }).flow
    }

    companion object {
        @Volatile
        private var instance: FakeRepository? = null
        private val existingAccounts = listOf(
            UserLoginInput(
                email = "admin",
                password = "pass",
            ),
            UserLoginInput(
                email = "teacher",
                password = "pass",
            ),
            UserLoginInput(
                email = "student",
                password = "pass",
            ),
        )


        fun getInstance(
            context: Context,
        ): FakeRepository = instance ?: synchronized(this) {
            FakeRepository(
                context,
            ).apply {
                instance = this
            }
        }
    }
}
