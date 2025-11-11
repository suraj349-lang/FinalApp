package com.spint.app.paging

//
//class EventsPagingSource(private  val eventsRepository: EventsRepository):
//    PagingSource<Int, DropProfileModel>() {
//    override fun getRefreshKey(state: PagingState<Int, DropProfileModel>): Int? {
//        return state.anchorPosition?.let { anchorPosition->
//            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DropProfileModel> {
//        val page=params.key?:1
//        return try {
//
//            val response=eventsRepository.getAllDropProfiles(page)
//            if (response.isSuccessful) {
//                val body = response.body()?.data ?: emptyList()
//                LoadResult.Page(
//                    data = body,
//                    prevKey = if (page == 1) null else page - 1,
//                    nextKey = if (body.isEmpty()) null else page + 1
//                )
//            }
//                else{
//                    LoadResult.Error(Exception())
//                }
//            }catch (e:IOException){
//                LoadResult.Error(e)
//            }catch (e:HttpException){
//                LoadResult.Error(e)
//            }
//        }
//    }
