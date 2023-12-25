package myplayground.example.learningq.network

import kotlinx.coroutines.delay
import myplayground.example.learningq.local_storage.LocalStorageManager
import myplayground.example.learningq.model.Class
import myplayground.example.learningq.model.Course
import myplayground.example.learningq.model.Feedback
import myplayground.example.learningq.model.Quiz
import myplayground.example.learningq.model.QuizQuestion
import myplayground.example.learningq.model.QuizQuestionType
import myplayground.example.learningq.model.Role
import myplayground.example.learningq.model.Token
import myplayground.example.learningq.model.User
import myplayground.example.learningq.network.request.AdminCreateUserRequest
import myplayground.example.learningq.network.request.LoginRequest
import myplayground.example.learningq.network.request.StudentCourseFetchRequest
import myplayground.example.learningq.network.response.LoginResponse
import myplayground.example.learningq.network.response.MeResponse
import myplayground.example.learningq.network.utils.WithCourses
import myplayground.example.learningq.network.utils.WithPagination
import myplayground.example.learningq.utils.CustomDayOfWeek
import myplayground.example.learningq.utils.TimeInSeconds
import myplayground.example.learningq.utils.removeDuplicatesBy
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import kotlin.math.ceil

class FakeApiService(val localStorageManager: LocalStorageManager) : ApiService {
    override suspend fun login(body: LoginRequest): LoginResponse {
        delay(1500)
        for (currentUser in listUser) {
            if (body.email.equals(
                    currentUser.email, ignoreCase = true
                ) && body.password == currentUser.password
            ) {
                return LoginResponse(
                    accessToken = "token ${currentUser.role}",
                    role = currentUser.role.toString(),
                )
            }
        }

        throw HttpException(
            Response.error<Token?>(400, "Invalid User/Password".toResponseBody())
        )
    }

    override suspend fun userMe(): MeResponse {
        delay(500)

        val token = localStorageManager.getUserToken()


        if (token.length <= "token ".length) {
            return MeResponse(
                studentUser,
            )
        }

        return when (token.substring("token ".length)) {
            "student" -> {
                MeResponse(
                    studentUser,
                )
            }

            "teacher" -> {
                MeResponse(
                    teacherUser,
                )

            }

            "admin" -> {
                MeResponse(
                    adminUser,
                )

            }

            "parent" -> {
                MeResponse(
                    parentUser,
                )

            }

            else -> MeResponse(
                studentUser,
            )
        }
    }

    override suspend fun fetchStudentQuiz(page: Int, limit: Int): WithPagination<List<Quiz>> {
        delay(1500)

        val startIndex = minOf((page - 1) * limit, QUIZ_LIST.size)
        val endIndex = minOf(startIndex + limit, QUIZ_LIST.size)

        return WithPagination(
            data = QUIZ_LIST.subList(startIndex, endIndex),
            page = page,
            status = "success",
            totalPage = ceil(QUIZ_LIST.size.toFloat() / limit.toFloat()).toInt(),
        )
    }

    override suspend fun fetchStudentQuizQuestion(quizId: String): List<QuizQuestion> {
        delay(500)

        return QUIZ_QUESTION_LIST.filter { it.quizId == quizId }
    }

    override suspend fun fetchStudentClasses(
        page: Int, limit: Int
    ): WithPagination<WithCourses<List<Class>>> {
        delay(1500)

        val startIndex = minOf((page - 1) * limit, CLASS_LIST.size)
        val endIndex = minOf(startIndex + limit, CLASS_LIST.size)

        return WithPagination(
            data = WithCourses(
                courses = CLASS_LIST.subList(startIndex, endIndex),
            ),
            page = page,
            status = "success",
            totalPage = ceil(CLASS_LIST.size.toFloat() / limit.toFloat()).toInt(),
        )
    }

    override suspend fun fetchStudentCoursesByClassId(body: StudentCourseFetchRequest): WithPagination<List<Course>> {
        delay(1500)

        val (classId, page, limit) = body

        val filteredCourseList = COURSE_LIST.filter { it.classId == classId }

        val startIndex = minOf((page - 1) * limit, filteredCourseList.size)
        val endIndex = minOf(startIndex + limit, filteredCourseList.size)

        return WithPagination(
            data = filteredCourseList.subList(startIndex, endIndex),
            page = page,
            status = "success",
            totalPage = ceil(filteredCourseList.size.toFloat() / limit.toFloat()).toInt(),
        )
    }

    override suspend fun fetchTeacherClassByTeacherUserId(teacherUserId: String): List<Class> {
        delay(400)

        return COURSE_LIST.map { course -> course.`class`!! }.removeDuplicatesBy { it.id }
    }

    override suspend fun fetchTeacherCoursesByTeacherUserId(
        teacherUserId: String,
        page: Int,
        limit: Int
    ): WithPagination<List<Course>> {
        delay(1500)

        val filteredCourseList = COURSE_LIST.filter { it.teacherUserId == teacherUserId }

        val startIndex = minOf((page - 1) * limit, filteredCourseList.size)
        val endIndex = minOf(startIndex + limit, filteredCourseList.size)

        return WithPagination(
            data = filteredCourseList.subList(startIndex, endIndex),
            page = page,
            status = "success",
            totalPage = ceil(filteredCourseList.size.toFloat() / limit.toFloat()).toInt(),
        )
    }

    override suspend fun fetchTeacherFeedbackByTeacherUserIdAndCourseId(
        teacherUserId: String,
        courseId: String
    ): List<Feedback> {
        delay(1000)

        return FEEDBACK_LIST.filter { it.teacherUserId == teacherUserId && it.quiz!!.courseId == courseId }
    }

    override suspend fun fetchFeedbackById(feedbackId: String): Feedback? {
        return FEEDBACK_LIST.find { it.id == feedbackId }
    }

    override suspend fun fetchTeacherQuiz(page: Int, limit: Int): WithPagination<List<Quiz>> {
        delay(1500)

        val startIndex = minOf((page - 1) * limit, QUIZ_LIST.size)
        val endIndex = minOf(startIndex + limit, QUIZ_LIST.size)

        return WithPagination(
            data = QUIZ_LIST.subList(startIndex, endIndex),
            page = page,
            status = "success",
            totalPage = ceil(QUIZ_LIST.size.toFloat() / limit.toFloat()).toInt(),
        )
    }

    override suspend fun fetchAdminClass(): List<Class> {
        return CLASS_LIST
    }

    override suspend fun fetchAdminUserTeacher(): List<User> {
        return listUser.filter { it.role == Role.Teacher }
    }

    override suspend fun fetchAdminCourses(page: Int, limit: Int): WithPagination<List<Course>> {
        delay(1500)

        val startIndex = minOf((page - 1) * limit, COURSE_LIST.size)
        val endIndex = minOf(startIndex + limit, COURSE_LIST.size)

        return WithPagination(
            data = COURSE_LIST.subList(startIndex, endIndex),
            page = page,
            status = "success",
            totalPage = ceil(COURSE_LIST.size.toFloat() / limit.toFloat()).toInt(),
        )
    }

    override suspend fun createUser(body: AdminCreateUserRequest) {
        TODO("Not yet implemented")
    }


    override suspend fun fetchUser(page: Int, limit: Int): WithPagination<List<User>> {
        delay(1500)

        val startIndex = minOf((page - 1) * limit, listUser.size)
        val endIndex = minOf(startIndex + limit, listUser.size)

        return WithPagination(
            data = listUser.subList(startIndex, endIndex),
            page = page,
            status = "success",
            totalPage = ceil(listUser.size.toFloat() / limit.toFloat()).toInt(),
        )
    }


    companion object {
        @Volatile
        private var instance: FakeApiService? = null

        val CLASS_LIST: List<Class> by lazy {

            val classList = mutableListOf(
                Class(
                    id = "1", name = "Kelas IX-1"
                ),
                Class(
                    id = "2", name = "Kelas IX-2"
                ),
            )
            classList.toList()
        }

        val COURSE_LIST: List<Course> by lazy {
            val courseList = mutableListOf(
                Course(
                    id = "1",
                    classId = "1",
                    name = "Matematika",
                    teacherUserId = "2",
                    dayOfWeek = CustomDayOfWeek.MONDAY,
                    startTimeInSeconds = TimeInSeconds(28800),
                    endTimeInSeconds = TimeInSeconds(54000),
                    description = null,

                    `class` = CLASS_LIST[0],
                ),
                Course(
                    id = "2",
                    classId = "1",
                    name = "Inggris",
                    teacherUserId = "2",
                    dayOfWeek = CustomDayOfWeek.TUESDAY,
                    startTimeInSeconds = TimeInSeconds(28800),
                    endTimeInSeconds = TimeInSeconds(54000),
                    description = null,

                    `class` = CLASS_LIST[0],
                ),
            )

            courseList.toList()
        }


        val QUIZ_LIST: List<Quiz> by lazy {
            val quizList = mutableListOf(
                Quiz(
                    id = "1",
                    courseId = COURSE_LIST[0].id,
                    name = "Quiz BAB II Penjumlahan, Pengurangan",
                    totalQuestion = 3,
                    course = COURSE_LIST[0],
                ),
                Quiz(
                    id = "2",
                    courseId = COURSE_LIST[1].id,
                    name = "Quiz BAB II Essay",
                    totalQuestion = 2,
                    course = COURSE_LIST[1],
                ),
                Quiz(
                    id = "3",
                    courseId = COURSE_LIST[1].id,
                    name = "Quiz BAB I.I",
                    totalQuestion = 3,
                    course = COURSE_LIST[1],
                    isCompleted = true,
                ),
                Quiz(
                    id = "4",
                    courseId = COURSE_LIST[1].id,
                    name = "Quiz BAB I.II",
                    totalQuestion = 1,
                    course = COURSE_LIST[1],
                    isCompleted = true,
                ),
            )

            quizList.toList()
        }

        val QUIZ_QUESTION_LIST: List<QuizQuestion> by lazy {
            val quizQuestionList = mutableListOf(
                QuizQuestion(
                    id = "1",
                    quizOrder = 1,
                    quizId = QUIZ_LIST[0].id,
                    title = "1 + 1",
                    quizType = QuizQuestionType.MultipleChoice,
                    multipleChoiceList = listOf("1", "2", "3", "4"),
                    multipleChoiceAnswerIndex = 2,
                ),
                QuizQuestion(
                    id = "2",
                    quizOrder = 2,
                    quizId = QUIZ_LIST[0].id,
                    title = "33 + 77",
                    quizType = QuizQuestionType.MultipleChoice,
                    multipleChoiceList = listOf("100", "110", "120", "130"),
                    multipleChoiceAnswerIndex = 2,
                ),
                QuizQuestion(
                    id = "3",
                    quizOrder = 3,
                    quizId = QUIZ_LIST[0].id,
                    title = "25-6",
                    quizType = QuizQuestionType.MultipleChoice,
                    multipleChoiceList = listOf("15", "17", "18", "19"),
                    multipleChoiceAnswerIndex = 4,
                ),
                QuizQuestion(
                    id = "4",
                    quizOrder = 1,
                    quizId = QUIZ_LIST[1].id,
                    title = "I ___ to school everyday.",
                    quizType = QuizQuestionType.MultipleChoice,
                    multipleChoiceList = listOf("go", "goes", "going", "am going"),
                    multipleChoiceAnswerIndex = 1,
                ),
                QuizQuestion(
                    id = "5",
                    quizOrder = 2,
                    quizId = QUIZ_LIST[1].id,
                    title = "Describe yourself.",
                    quizType = QuizQuestionType.Essay,
                ),
            )

            quizQuestionList.toList()
        }

        val FEEDBACK_LIST: List<Feedback> by lazy {
            val feedbackList = mutableListOf(
                Feedback(
                    id = "1",
                    quizId = QUIZ_LIST[2].id,
                    studentUserId = studentUser.id,
                    teacherUserId = teacherUser.id,
                    content = "Guru ini selalu memberikan kesempatan kepada siswa untuk berbicara",
                    quiz = QUIZ_LIST[2],
                    studentUser = studentUser,
                    teacherUser = teacherUser,
                ),
                Feedback(
                    id = "2",
                    quizId = QUIZ_LIST[3].id,
                    studentUserId = studentUser.id,
                    teacherUserId = teacherUser.id,
                    content = "Guru ini memberikan materi tambahan yang sangat membantu.",
                    quiz = QUIZ_LIST[3],
                    studentUser = studentUser,
                    teacherUser = teacherUser,
                ),
                Feedback(
                    id = "2",
                    quizId = QUIZ_LIST[0].id,
                    studentUserId = studentUser.id,
                    teacherUserId = teacherUser.id,
                    content = "Saya merasa guru ini memberikan nilai lebih dalam proses pembelajaran",
                    quiz = QUIZ_LIST[0],
                    studentUser = studentUser,
                    teacherUser = teacherUser,
                ),
            )

            feedbackList
        }


        val studentUser = User(
            id = "1",
            email = "student.one@gmail.com",
            name = "Student",
            password = "12345",
//            image_url = "https://miro.medium.com/v2/resize:fill:110:110/1*x1I-A7aVdqWFelvJakKWBg.jpeg",
            role = Role.Student,
        )

        val teacherUser = User(
            id = "2",
            email = "teacher.one@gmail.com",
            name = "Teacher",
            password = "12345",
//            image_url = "https://miro.medium.com/v2/resize:fill:110:110/1*x1I-A7aVdqWFelvJakKWBg.jpeg",
            role = Role.Teacher,
        )

        val adminUser = User(
            id = "3",
            email = "admin.one@gmail.com",
            name = "Admin",
            password = "12345",
//            image_url = "https://miro.medium.com/v2/resize:fill:110:110/1*x1I-A7aVdqWFelvJakKWBg.jpeg",
            role = Role.Admin,
        )

        val parentUser = User(
            id = "4",
            email = "parent.one@gmail.com",
            name = "Parent",
            password = "12345",
//            image_url = "https://miro.medium.com/v2/resize:fill:110:110/1*x1I-A7aVdqWFelvJakKWBg.jpeg",
            role = Role.Parent,

            studentUserId = studentUser.id,
            studentUser = studentUser,
        )

        val listUser = listOf(
            studentUser, teacherUser, adminUser, parentUser
        )

//        val USER_LIST: List<User> by lazy {
//
//            val userList = mutableListOf<User>()
//            for (i in 1..30) {
//                val user = User(
//                    id = "$i",
//                    name = "User $i",
//                    role = Role.Student,
//                    imageUrl = null,
//                    email = "$i@gmail.com",
//                    password = "12345",
//                )
//                userList.add(user)
//            }
//
//            userList.toList()
//        }

        fun getInstance(
            localStorageManager: LocalStorageManager,
        ): FakeApiService = instance ?: synchronized(this) {
            FakeApiService(
                localStorageManager = localStorageManager,
            ).apply {
                instance = this
            }
        }
    }
}