package com.spint.app.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.spint.app.model.DirectChat
import com.spint.app.repository.EventsRepository
import retrofit2.HttpException
import java.io.IOException

class DirectChatUsersPagingSource(
    private val eventsRepository: EventsRepository,
    private val userId:String,
    private val lat: Double,
    private val long: Double,
) : PagingSource<Int, DirectChat>() {

    override fun getRefreshKey(state: PagingState<Int, DirectChat>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,DirectChat  > {
        val page = params.key ?: 1
        return try {
            val response = eventsRepository.getAllDirectChatUsers(userId,lat, long, page)

            if (response.isSuccessful) {
                val body = response.body()?.data ?: emptyList() // 🔹 Ensure `body.data` is used

                LoadResult.Page(
                    data = body,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (body.isEmpty()) null else page + 1
                )

            } else {
                LoadResult.Error(Exception("API Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
