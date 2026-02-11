package com.spint.app.screens._2pings

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.repository.EventsRepository
import retrofit2.HttpException
import java.io.IOException

class FlashPostsPagingSource(private  val eventsRepository: EventsRepository):
    PagingSource<Int, FlashPostResponse>() {
    override fun getRefreshKey(state: PagingState<Int, FlashPostResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition->
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FlashPostResponse> {
        val page=params.key?:1
        return try {

            val response=eventsRepository.getAllFlashPosts(page)
            if (response.success) {
                val body = response.data
                LoadResult.Page(
                    data = body,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (body.isEmpty()) null else page + 1
                )
            }
            else{
                LoadResult.Error(Exception())
            }
        }catch (e:IOException){
            LoadResult.Error(e)
        }catch (e:HttpException){
            LoadResult.Error(e)
        }
    }
}

