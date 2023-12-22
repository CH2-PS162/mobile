package myplayground.example.learningq.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import myplayground.example.learningq.model.Course
import myplayground.example.learningq.network.ApiService
import myplayground.example.learningq.network.request.StudentCourseFetchRequest
import retrofit2.HttpException
import java.io.IOException

class AdminCoursePagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, Course>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
        val currentPage = params.key ?: 1

        return try {
            val response = apiService.fetchAdminCourses(
                page = currentPage,
                limit = params.loadSize,
            )
            val courses = response.data

            LoadResult.Page(
                data = courses,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (courses.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            if (exception.code() == 404) {
                return LoadResult.Page(
                    data = listOf(),
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = null,
                )
            } else {
                return LoadResult.Error(exception)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}