package myplayground.example.learningq.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import myplayground.example.learningq.model.Class
import myplayground.example.learningq.model.Course
import myplayground.example.learningq.model.Feedback
import myplayground.example.learningq.model.Quiz
import myplayground.example.learningq.model.QuizQuestion
import myplayground.example.learningq.model.Token
import myplayground.example.learningq.model.User
import myplayground.example.learningq.network.ApiService

interface Repository {
    suspend fun userLogin(request: UserLoginInput, apiService: ApiService): Token?
    fun userRegister(request: UserRegisterInput): Flow<Token?>
    suspend fun userMe(token: String, apiService: ApiService): User?

    suspend fun fetchStudentQuizPaging(apiService: ApiService): Flow<PagingData<Quiz>>
    suspend fun fetchStudentQuizQuestionByQuizId(
        quizId: String,
        apiService: ApiService
    ): List<QuizQuestion>

    suspend fun fetchStudentClassPaging(apiService: ApiService): Flow<PagingData<Class>>

    suspend fun fetchStudentCourseByClassId(
        classId: String,
        apiService: ApiService
    ): Flow<PagingData<Course>>

    suspend fun fetchTeacherQuizPaging(apiService: ApiService): Flow<PagingData<Quiz>>

    suspend fun fetchTeacherClassByTeacherUserId(
        teacherUserId: String,
        apiService: ApiService
    ): List<Class>

    suspend fun fetchTeacherFeedbackByTeacherUserIdAndCourseId(
        teacherUserId: String,
        courseId: String,
        apiService: ApiService,
    ): List<Feedback>

    suspend fun fetchFeedbackById(
        feedbackId: String,
        apiService: ApiService,
    ): Feedback?

    suspend fun fetchTeacherCoursePaging(
        teacherId: String,
        apiService: ApiService
    ): Flow<PagingData<Course>>

    suspend fun fetchAdminClassPaging(
        apiService: ApiService,
    ): Flow<PagingData<Class>>

    suspend fun fetchAdminCoursePaging(
        apiService: ApiService,
    ): Flow<PagingData<Course>>

    suspend fun fetchUserPaging(apiService: ApiService): Flow<PagingData<User>>
}


//class LearningQRepository(
//    context: Context,
//) : Repository {
//
//    override suspend fun userLogin(request: UserLoginInput, apiService: ApiService): Token? {
//        val loginResponse = apiService.login(
//            LoginRequest(
//                request.email,
//                request.password,
//            ),
//        )
//
//        return Token(
//            auth_token = loginResponse.accessToken,
//            role = loginResponse.role,
//        )
//    }
//
//    override fun userRegister(request: UserRegisterInput): Flow<Token?> {
//        return flow {
//            emit(null)
//        }
//    }
//
//    override suspend fun userMe(token: String): User? {
//        delay(1500)
//
//        val studentUser = User(
//            id = "1",
//            name = "Student",
//            image_url = "https://miro.medium.com/v2/resize:fill:110:110/1*x1I-A7aVdqWFelvJakKWBg.jpeg",
//            role = Role.Student,
//        )
//
//        val teacherUser = User(
//            id = "2",
//            name = "Teacher",
//            image_url = "https://miro.medium.com/v2/resize:fill:110:110/1*x1I-A7aVdqWFelvJakKWBg.jpeg",
//            role = Role.Teacher,
//        )
//
//        val adminUser = User(
//            id = "3",
//            name = "Admin",
//            image_url = "https://miro.medium.com/v2/resize:fill:110:110/1*x1I-A7aVdqWFelvJakKWBg.jpeg",
//            role = Role.Admin,
//        )
//
//        if (token.length <= "token ".length) {
//            return studentUser
//        }
//
//        return when (token.substring("token ".length)) {
//            "student" -> {
//                studentUser
//            }
//
//            "teacher" -> {
//                teacherUser
//            }
//
//            "admin" -> {
//                adminUser
//            }
//
//            else -> studentUser
//        }
//    }
//
//    override suspend fun fetchStudentQuizPaging(apiService: ApiService): Flow<PagingData<Quiz>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 10,
//                initialLoadSize = 10,
//                prefetchDistance = 2
//            ),
//            pagingSourceFactory = {
//                StudentQuizPagingSource(
//                    apiService,
//                )
//            }).flow
//    }
//
//    override suspend fun fetchStudentClassPaging(apiService: ApiService): Flow<PagingData<Class>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 7,
//                initialLoadSize = 7,
//                prefetchDistance = 2
//            ),
//            pagingSourceFactory = {
//                StudentClassPagingSource(
//                    apiService,
//                )
//            }).flow
//    }
//
//    override suspend fun fetchTeacherQuizPaging(apiService: ApiService): Flow<PagingData<Quiz>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 10,
//                initialLoadSize = 10,
//                prefetchDistance = 2
//            ),
//            pagingSourceFactory = {
//                TeacherQuizPagingSource(
//                    apiService,
//                )
//            }).flow
//    }
//
//    override suspend fun fetchUserPaging(apiService: ApiService): Flow<PagingData<User>> {
//        TODO("not implemented")
////        return Pager(
////            config = PagingConfig(
////                pageSize = 10,
////                initialLoadSize = 10,
////                prefetchDistance = 2
////            ),
////            pagingSourceFactory = {
////                TeacherQuizPagingSource(
////                    apiService,
////                )
////            }).flow
//    }
//
//    companion object {
//        @Volatile
//        private var instance: LearningQRepository? = null
//        private val existingAccounts = listOf(
//            UserLoginInput(
//                email = "admin",
//                password = "pass",
//            ),
//            UserLoginInput(
//                email = "teacher",
//                password = "pass",
//            ),
//            UserLoginInput(
//                email = "student",
//                password = "pass",
//            ),
//        )
//
//        fun getInstance(
//            context: Context,
//        ): LearningQRepository = instance ?: synchronized(this) {
//            LearningQRepository(
//                context,
//            ).apply {
//                instance = this
//            }
//        }
//    }
//}
