package com.spint.app.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.spint.app.model.DropProfileResponse
import com.spint.app.repository.EventsRepository
import retrofit2.HttpException
import java.io.IOException

class DropProfilePagingSource(private  val eventsRepository: EventsRepository):
    PagingSource<Int, DropProfileResponse>() {
    override fun getRefreshKey(state: PagingState<Int, DropProfileResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition->
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DropProfileResponse> {
        val page=params.key?:1
        return try {

            val response=eventsRepository.getAllDropProfiles(page)
            if (response.isSuccessful) {
                val body = response.body()?.data ?: emptyList()
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
