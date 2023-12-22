package myplayground.example.learningq.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import myplayground.example.learningq.model.Class
import myplayground.example.learningq.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class AdminClassPagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, Class>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Class> {
        val currentPage = params.key ?: 1

        return try {
            val response = apiService.fetchStudentClasses(
                currentPage,
                params.loadSize,
            )
            val `class` = response.data

            LoadResult.Page(
                data = `class`.courses,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (`class`.courses.isEmpty()) null else currentPage + 1
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

    override fun getRefreshKey(state: PagingState<Int, Class>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}