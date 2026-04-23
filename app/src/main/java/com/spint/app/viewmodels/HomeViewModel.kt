package com.spint.app.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.spint.app.datastore.StoreUserState
import com.spint.app.model.ChatList
import com.spint.app.model.DirectChat
import com.spint.app.model.DirectChatRequest
import com.spint.app.model.DropProfileResponse
import com.spint.app.model.Event
import com.spint.app.model.EventResponse
import com.spint.app.model.GetDropProfileResponseModel
import com.spint.app.model.PremiumEventResponseDTO
import com.spint.app.model.User
import com.spint.app.model.flashPost.FlashPostRequestDto
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.paging.DirectChatUsersPagingSource
import com.spint.app.paging.DropProfilePagingSource
import com.spint.app.repository.ChatDatabaseRepository
import com.spint.app.repository.EventsRepository
import com.spint.app.repository.ProfileRepository
import com.spint.app.screens._1home._1FlashPosts.data.FlashPostsPagingSource
import com.spint.app.utils.UserObject
import com.spint.app.utils.RequestState
import com.google.android.libraries.places.api.Places
import com.spint.app.datastore.StoreLoginState
import com.spint.app.model.flashPost.CommentRequest
import com.spint.app.model.flashPost.CommentResponse
import com.spint.app.model.flashPost.FlashPostDetailsResponse
import com.spint.app.model.flashPost.PingsOnFlashPostRequest
import com.spint.app.model.flashPost.PingsOnFlashPostResponse
import com.spint.app.model.places.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File

import javax.inject.Inject
import kotlin.Exception


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val chatDatabaseRepository: ChatDatabaseRepository,
    private val profileRepository: ProfileRepository,
    private val s3Uploader: S3Uploader,
    private val storeUserState: StoreUserState,
    private val storeLoginState: StoreLoginState,
    @ApplicationContext context: Context): ViewModel(){

    val TAG="GET_EVENTS_RESPONSE";


    private val placesClient by lazy { Places.createClient(context) }
    var checked= mutableStateOf(false)
    var shareProfileClicked= MutableStateFlow(false)
    init {
        viewModelScope.launch {
            val user = storeUserState.getUserFromDataStore.firstOrNull()
            if (user != null) {
                UserObject.updateUser(user)
            } else {
                Log.w("User", "No user found in DataStore!")
            }
        }
        viewModelScope.launch {
            //getAllEvents()
        }
    }

    //-------------------------------------------------------------------------------------------------------//
    val dropProfileUploadUri= mutableStateOf(Uri.EMPTY)
    val showDropDialog= mutableStateOf(false)

    //--------------------------------------------------------------------------------------------------------------------------------------------//
    private val _triggerFetch = MutableStateFlow(false)
    val triggerFetch: StateFlow<Boolean> = _triggerFetch.asStateFlow()

    // Create Pager but don't collect initially
    private val _droppedProfilesFlow = MutableStateFlow<Flow<PagingData<DropProfileResponse>>?>(null)
    val droppedProfiles: StateFlow<Flow<PagingData<DropProfileResponse>>?> = _droppedProfilesFlow.asStateFlow()
    private val _shouldLoadDroppedProfiles= MutableStateFlow(false)
    val shouldLoadDroppedProfiles:StateFlow<Boolean>  = _shouldLoadDroppedProfiles

    fun getDefaultDropProfiles(location:String) {
        _droppedProfilesFlow.value = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 5),
            pagingSourceFactory = { DropProfilePagingSource(eventsRepository) }
        ).flow.cachedIn(viewModelScope)

        _triggerFetch.value = true
    }

    fun resetShouldLoadDroppedProfiles(){
        _shouldLoadDroppedProfiles.value=true
    }

    //-------------------------------------------DIRECT CHAT--------------------------------------------------------------------------------------//
    /*
    val directChatRequestState = MutableStateFlow<RequestState<String>>(RequestState.Idle)
    val directChatResponse:MutableState<RequestState<DirectChat>> = mutableStateOf(RequestState.Idle)
    fun sendDirectChatData(data: DirectChatRequest)=viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.setLocationForDirectChat(data)
            .onStart {
                directChatResponse.value=RequestState.Loading;
            }
            .catch {
                Log.d("Data received","error found")
                directChatResponse.value=RequestState.Error(it)
                directChatRequestState.emit(RequestState.Error(it))
            }
            .collect {
                directChatResponse.value = RequestState.Success(it.data);
                // getNearByUsers(data.lat,data.long)
                loadDirectChatUsers(data.lat,data.long)
            }
    }

    private val nearByUserResponse = MutableStateFlow<Flow<PagingData<DirectChat>>?>(null)
    val nearByUsersList: StateFlow<Flow<PagingData<DirectChat>>?> = nearByUserResponse.asStateFlow()

    fun loadDirectChatUsers(lat:Double,long:Double) {
        viewModelScope.launch {
            nearByUserResponse.value = Pager(
                config = PagingConfig(pageSize = 10, prefetchDistance = 5),
                pagingSourceFactory = { DirectChatUsersPagingSource(eventsRepository, lat, long) }
            ).flow.cachedIn(viewModelScope).onStart {  }.catch {  }.onEach {  }
        }
    }
*/
    // Request state tracking for Direct Chat
    private val _directChatRequestState = MutableStateFlow<RequestState<String>>(RequestState.Idle)
    val directChatRequestState: StateFlow<RequestState<String>> = _directChatRequestState.asStateFlow()

    // Response state tracking
    private val _directChatResponse = MutableStateFlow<RequestState<DirectChat>>(RequestState.Idle)
    val directChatResponse: StateFlow<RequestState<DirectChat>> = _directChatResponse.asStateFlow()

    // Nearby user paging response
    private val _nearByUserResponse = MutableStateFlow<PagingData<DirectChat>>(PagingData.empty())
    val nearByUsersList: StateFlow<PagingData<DirectChat>> = _nearByUserResponse.asStateFlow()

    fun sendDirectChatData(data: DirectChatRequest) = viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.setLocationForDirectChat(data)
            .onStart {
                _directChatResponse.value = RequestState.Loading
            }
            .catch { exception ->
                Log.d("Data received", "Error found: ${exception.message}")
                _directChatResponse.value = RequestState.Error(exception)
                _directChatRequestState.emit(RequestState.Error(exception))
            }
            .collect { response ->
                _directChatResponse.value = RequestState.Success(response.data)
                // Fetch nearby users after successful chat request
                loadDirectChatUsers(data.userId,data.lat, data.long)
            }
    }

    fun loadDirectChatUsers(userId: String, lat: Double, long: Double) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 10, prefetchDistance = 5),
                pagingSourceFactory = { DirectChatUsersPagingSource(eventsRepository, userId,lat, long) }
            ).flow
                .cachedIn(viewModelScope)
                .onStart {
                    _directChatRequestState.emit(RequestState.Loading) // Show loading UI
                }
                .catch { exception ->
                    _directChatRequestState.emit(RequestState.Error(exception))
                }
                .collect { pagingData ->
                    _nearByUserResponse.value = pagingData
                    _directChatRequestState.emit(RequestState.Success("Data loaded successfully"))
                }
        }
    }

    // Nearby user paging response
    private val _removeUserResponse = MutableStateFlow<RequestState<String>>(RequestState.Idle)
    val removeUserResponse: StateFlow<RequestState<String>> = _removeUserResponse.asStateFlow()

    fun removeUserFromDirectChat(id:String) = viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.removeUserFromDirectChat(id)
            .onStart {
                _removeUserResponse.value = RequestState.Loading
            }
            .catch { exception ->
                Log.e("Data received remove", "Error found: ${exception.message}",exception)
                _removeUserResponse.value = RequestState.Error(exception)
            }
            .collect { response ->
                _removeUserResponse.value = RequestState.Success(response.data)
            }
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------//


//    fun getAutocompletePredictions(query: String): Flow<List<AutocompletePrediction>> {
//        val results = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
//
//        val request = FindAutocompletePredictionsRequest.builder()
//            .setQuery(query)
//            .build()
//
//        placesClient.findAutocompletePredictions(request)
//            .addOnSuccessListener { response ->
//                Log.i("PlacesViewModel", "Autocomplete prediction request success $response")
//                results.value = response.autocompletePredictions
//            }
//            .addOnFailureListener { exception ->
//                Log.e("PlacesViewModel", "Autocomplete prediction request failed: ${exception.message}")
//            }
//
//        return results
//    }

    var results by mutableStateOf<List<Place>>(emptyList())
        private set

    fun getAutocompletePredictions(query: String) {
        viewModelScope.launch {
            results = eventsRepository.getPlaces(query)
        }
    }


//    val imageUri= mutableStateOf<Uri>(Uri.EMPTY)
//    val imageUploadResponse: MutableState<RequestState<ImageUploadResponse>> = mutableStateOf(RequestState.Idle)

//     fun uploadImage(uri: Uri,context: Context) {
//        viewModelScope.launch {
//           repository.uploadImage(uri, context )
//                .onStart {
//                    imageUploadResponse.value=RequestState.Loading
//                }
//                .catch {
//                    imageUploadResponse.value=RequestState.Error(it)
//                }
//                .collect{
//                    imageUploadResponse.value=RequestState.Success(it)
//                }
//        }
//    }



    //--------------------------------------------------------------------------------------------------------------------//
    private val _eventsListResponse = MutableStateFlow<RequestState<List<EventResponse>>>(RequestState.Idle)
    val eventsListResponse: StateFlow<RequestState<List<EventResponse>>> = _eventsListResponse.asStateFlow()

    fun getAllEvents()=viewModelScope.launch(Dispatchers.IO) {
        val TAG="GET_ALL_EVENTS_RESPONSE";
        eventsRepository.getAllEvents()
            .onStart {
                _eventsListResponse.value = RequestState.Loading
                Log.d(TAG, "all profiles start ${_eventsListResponse.value}")
            }.catch {
                _eventsListResponse.value = RequestState.Error(it)
                Log.d(TAG, "all profiles error ${_eventsListResponse.value}")
            }.collect {
                _eventsListResponse.value = RequestState.Success(it.data)
                Log.d(TAG, "all profiles data ${_eventsListResponse.value}")
            }
    }

    //----------------------------Get user events for Profile----------------------------------------------------------------------------------------//
    private val _userEventsListResponse = MutableStateFlow<RequestState<List<EventResponse>>>(RequestState.Idle)
    val userEventsListResponse: StateFlow<RequestState<List<EventResponse>>> = _userEventsListResponse.asStateFlow()
    val canFetchEvents = mutableStateOf(true)

    fun getUserEvents(id:String)=viewModelScope.launch(Dispatchers.IO) {
        val TAG="GET_EVENTS_RESPONSE";
        eventsRepository.getUserEvents(id)
            .onStart {
                _userEventsListResponse.value = RequestState.Loading

            }.catch {
                _userEventsListResponse.value = RequestState.Error(it)
                Log.d(TAG, "user events error ${_userEventsListResponse.value}")

            }.collect {
                _userEventsListResponse.value = RequestState.Success(it.data)
                canFetchEvents.value=false
                Log.d(TAG, "user events data ${_userEventsListResponse.value}")

            }
    }
    //----------------------------Get user events for Profile----------------------------------------------------------------------------------------//
    private val _eventDetailsResponse = MutableStateFlow<RequestState<EventResponse>>(RequestState.Idle)
    val eventDetailsResponse: StateFlow<RequestState<EventResponse>> = _eventDetailsResponse.asStateFlow()

    fun getEventDetails(id:String)=viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.getEventDetails(id)
            .onStart {
                _eventDetailsResponse.value = RequestState.Loading

            }.catch { e->
                _eventDetailsResponse.value = RequestState.Error(e)
                Log.e(TAG, "user events error ${_eventDetailsResponse.value}, ${e.message}",e)

            }.collect {
                _eventDetailsResponse.value = RequestState.Success(it.data)
                Log.d(TAG, "user events data ${_eventDetailsResponse.value}")

            }
    }

    //----------------------------Get user events for Profile----------------------------------------------------------------------------------------//
    private val _upvoteEvent = MutableStateFlow<RequestState<String>>(RequestState.Idle)
    val upvoteEvent: StateFlow<RequestState<String>> = _upvoteEvent.asStateFlow()
    fun upvoteEvent(id:String)=viewModelScope.launch(Dispatchers.IO) {
        val TAG="";
        eventsRepository.upvoteEvent(id)
            .onStart {
                _upvoteEvent.value = RequestState.Loading

            }.catch {
                _upvoteEvent.value = RequestState.Error(it)
                Log.d(TAG, "user events error ${_upvoteEvent.value}")

            }.collect {
                _upvoteEvent.value = RequestState.Success(it)
                Log.d(TAG, "user events data ${_upvoteEvent.value}")

            }
    }

    //----------------------------------Get users Dropped Profiles----------------------------------------------------------------------------------//
    private val _userDropProfilesListResponse = MutableStateFlow<RequestState<GetDropProfileResponseModel>>(RequestState.Idle)
    val userDropProfilesListResponse: StateFlow<RequestState<GetDropProfileResponseModel>> = _userDropProfilesListResponse.asStateFlow()
    val canFetchDroppedProfiles= mutableStateOf(true)
    fun getUserDropProfiles(id:String)=viewModelScope.launch(Dispatchers.IO) {
        val TAG="GET_EVENTS_RESPONSE_drop";
        eventsRepository.getUserDropProfiles(id)
            .onStart {
                _userDropProfilesListResponse.value = RequestState.Loading

            }.catch {
                _userDropProfilesListResponse.value = RequestState.Error(it)
                Log.d(TAG, "user drop profile error $it")

            }.collect {
                _userDropProfilesListResponse.value = RequestState.Success(it)
                canFetchDroppedProfiles.value=false
                Log.d(TAG, "user drop profile data ${_userDropProfilesListResponse.value}")

            }
    }
//---------------------------------Create event ----------------------------------------------//
    private val _imageUploadStatus=MutableStateFlow<RequestState<String>>(RequestState.Idle )
    val imageUploadStatus:StateFlow<RequestState<String>> = _imageUploadStatus

    fun uploadImageAndThenCreateEvent(userId: String, file: File, onSuccess: (url:String) -> Unit) {
        viewModelScope.launch {
            try {
                _imageUploadStatus.value = RequestState.Loading
                Log.i("uploadCreateImage", "uploadImageAndThen: called in viewModel")
                val signedUrl = s3Uploader.getPreSignedUrl(userId)
                val success = s3Uploader.uploadToS3(signedUrl.url, file)
                if (success) {
                    _imageUploadStatus.value = RequestState.Success(signedUrl.key)
                    onSuccess(signedUrl.key)
                } else {
                    _imageUploadStatus.value = RequestState.Error(Exception("Upload failed"))
                }
            } catch (e: Exception) {
                _imageUploadStatus.value = RequestState.Error(e)
            }
        }
    }

    var createEventResponse = MutableStateFlow<RequestState<String>>(RequestState.Idle)
    fun createEvent(data:Event){
        createEventResponse.value=RequestState.Loading
        viewModelScope.launch(Dispatchers.IO){
            eventsRepository.createEvent(data)
                .catch {
                    Log.e(TAG, "createEvent: ${it.printStackTrace()}",it )
                    createEventResponse.value=RequestState.Error(it)
                }.collect{
                    createEventResponse.value=RequestState.Success(it.data)
                }
        }
    }
    fun resetEventResponseState(){
        createEventResponse.value=RequestState.Idle
    }



//----------------------------------------------------------------------------------------------------------------------------------//

    val premiumCreateEventResponse:MutableState<RequestState<PremiumEventResponseDTO>> = mutableStateOf(RequestState.Idle)
    var premiumCreateEventKey :MutableState<Int> = mutableStateOf(0);
    fun premiumCreateEvent(event:Event)=viewModelScope.launch(Dispatchers.IO) {
        val TAG="PREMIUM_CREATE_EVENT_RESPONSE"
        eventsRepository.sendPremiumCreateEventData(event)
            .onStart {
                premiumCreateEventResponse.value=RequestState.Loading;
                Log.d(TAG,premiumCreateEventResponse.value.toString())
            }
            .catch {
                Log.d(TAG,"error found")
                premiumCreateEventResponse.value=RequestState.Error(it)
                Log.d(TAG,premiumCreateEventResponse.value.toString())
            }
            .collect {
                premiumCreateEventResponse.value = RequestState.Success(it);
                Log.d(TAG,premiumCreateEventResponse.value.toString())
            }
    }
   //----------------------------------------------------------------------------------------------------------------------------------//
   //----------------------------------------FLASH POST----------------------------------------------------------------------------//

    private val _location = MutableStateFlow<String?>(null)

    val flashPostsFlow = Pager(config = PagingConfig(pageSize = 10, prefetchDistance = 1), pagingSourceFactory = { FlashPostsPagingSource(eventsRepository) }).flow.cachedIn(viewModelScope)

    fun getAllFlashPosts(location: String) {
        _location.value = location     // this is the ONLY change
    }
                                            ///--------------------------///
   private val _userPingsListResponse = MutableStateFlow<RequestState<List<FlashPostResponse>>>(RequestState.Idle)
    val userPingsListResponse: StateFlow<RequestState<List<FlashPostResponse>>> = _userPingsListResponse.asStateFlow()
    val canFetchPings = mutableStateOf(true)

    fun getUserFlashPosts(id:String)=viewModelScope.launch(Dispatchers.IO) {
        val TAG="Get_Flash_Posts_Response";
        eventsRepository.getUserPings(id)
            .onStart {
                _userPingsListResponse.value = RequestState.Loading

            }.catch {
                _userPingsListResponse.value = RequestState.Error(it)
                Log.d(TAG, "user flash post error ${_userPingsListResponse.value}")

            }.collect {
                _userPingsListResponse.value = RequestState.Success(it.data)
                canFetchPings.value=false
                Log.d(TAG, "user flash post data ${_userPingsListResponse.value}")

            }
    }
    //============================================================================================================//

    // 🔥 Prevent multiple calls per post in session
    private val viewedPosts = mutableSetOf<String>()

    fun registerView(postId: String,userId: String) {

        //  Already viewed → skip
        if (viewedPosts.contains(postId)) return

        viewedPosts.add(postId)

        viewModelScope.launch {
            try {
                eventsRepository.registerFlashPostView(postId,userId)
            } catch (e: Exception) {
                // optional: retry or log
            }
        }
    }

    //============================================================================================================//

    private val _userFlashPostDetailsResponse = MutableStateFlow<RequestState<FlashPostDetailsResponse>>(RequestState.Idle)
    val userFlashPostDetailsResponse: StateFlow<RequestState<FlashPostDetailsResponse>> = _userFlashPostDetailsResponse.asStateFlow()

    fun getUserFlashPostDetails(id:String)=viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.getUserFlashPostDetails(id)
            .onStart {
                _userFlashPostDetailsResponse.value = RequestState.Loading

            }.catch {
                _userFlashPostDetailsResponse.value = RequestState.Error(it)
                Log.d("getUserFlashPostDetails", "user flash post details error ${_userFlashPostDetailsResponse.value}",it)

            }.collect {
                _userFlashPostDetailsResponse.value = RequestState.Success(it.data)
                canFetchPings.value=false
                Log.d("getUserFlashPostDetails", "user flash post details data ${_userFlashPostDetailsResponse.value}")

            }
    }
    private var _createFlashPostResponse:MutableStateFlow<RequestState<String>> = MutableStateFlow(RequestState.Idle)
    var createFlashPostResponse :StateFlow<RequestState<String>> = _createFlashPostResponse

    fun createFlashPost(ping:FlashPostRequestDto)=viewModelScope.launch(Dispatchers.IO) {
        val tag="CREATE_PING_RESPONSE"

        eventsRepository.createFlashPost(ping)
            .onStart {
                _createFlashPostResponse.value=RequestState.Loading;

                Log.d(tag,_createFlashPostResponse.value.toString())
            }
            .catch {

                Log.e(tag,it.printStackTrace().toString())
                _createFlashPostResponse.value=RequestState.Error(it)
                Log.e(tag,it.message.toString())
            }
            .collect {
                if(it.success.uppercase() =="TRUE") {
                    Log.i(tag, "createPing: success")
                    _createFlashPostResponse.value = RequestState.Success(it.data);
                }else{
                    Log.e(tag, it.message)
                   // _createPingResponse.value = RequestState.Error(it);
                }

            }
    }

    fun resetCreatePingResponseState(){
        _createFlashPostResponse.value=RequestState.Idle
    }
    private val _pingOnPostRequest=MutableStateFlow<RequestState<PingsOnFlashPostResponse>>(RequestState.Idle)
    val pingOnPostRequest: StateFlow<RequestState<PingsOnFlashPostResponse>> = _pingOnPostRequest

    fun addPingOnFlashPost(pingsOnFlashPostRequest: PingsOnFlashPostRequest){
        viewModelScope.launch {
            eventsRepository.addPingToFlashPost(pingsOnFlashPostRequest)
                .onStart{}
                .catch {error->
                    Log.e("addPingOnFlashPost", "addPingOnFlashPost: ${error.fillInStackTrace()}",error )
                }
                .collect {
                    Log.i("addPingOnFlashPost", "addPingOnFlashPost: $pingOnPostRequest")
                    _pingOnPostRequest.value= RequestState.Success(it)
                }
        }
    }

//====================================================================================================================================//
    val _flashPostComments = MutableStateFlow<RequestState<List<CommentResponse>>> (RequestState.Idle)
    val flashPostComments: StateFlow<RequestState<List<CommentResponse>>> = _flashPostComments

    fun getFlashPostComments(pingId:String)= viewModelScope.launch {
            eventsRepository.getFlashPostComments(pingId)
                .onStart {
                    _flashPostComments.value= RequestState.Loading
                }
                .catch {ex->
                    _flashPostComments.value= RequestState.Error(ex)
                }
                .collect { value ->
                    _flashPostComments.value= RequestState.Success(value.data)
                }
    }
    //====================================================================================================================================//
    val _addFlashPostComment = MutableStateFlow<RequestState<CommentResponse>> (RequestState.Idle)
    val addFlashPostComment: StateFlow<RequestState<CommentResponse>> = _addFlashPostComment

    fun addFlashPostComments(commentRequest: CommentRequest)= viewModelScope.launch {
        eventsRepository.addFlashPostComments(commentRequest)
            .onStart {
                _addFlashPostComment.value= RequestState.Loading
            }
            .catch {ex->
                _addFlashPostComment.value= RequestState.Error(ex)
            }
            .collect { value ->
                _addFlashPostComment.value= RequestState.Success(value.data)
                val currentState = _flashPostComments.value

                if (currentState is RequestState.Success) {
                    val updatedList = listOf(value.data) + currentState.data
                    _flashPostComments.value = RequestState.Success(updatedList)
                }
            }
    }

    fun sendReply(commentRequest: CommentRequest) {
        viewModelScope.launch {
            eventsRepository.addFlashPostComments(commentRequest)
                .onStart {

                }
                .catch {  }
                .collect {

                }
        }
    }
    //====================================================================================================================================//
    val _deleteFlashPostComment = MutableStateFlow<RequestState<String>> (RequestState.Idle)
    val deleteFlashPostComment: StateFlow<RequestState<String>> = _deleteFlashPostComment

    fun deleteFlashPostComments(pingId:String)= viewModelScope.launch {
        eventsRepository.deleteFlashPostComments(pingId)
            .onStart {
                _deleteFlashPostComment.value= RequestState.Loading
            }
            .catch {ex->
                _deleteFlashPostComment.value= RequestState.Error(ex)
            }
            .collect { value ->
                _deleteFlashPostComment.value= RequestState.Success(value.data)
            }
    }



//--------------------------------------------------------------------------------------------------------------------//

    private val _saveUserToChatListResponseState= MutableStateFlow<RequestState<ChatList>>(RequestState.Idle)
    val saveUserToChatListResponseState: StateFlow<RequestState<ChatList>> = _saveUserToChatListResponseState
    val saveToChatListSuccess= MutableStateFlow<Boolean>(false)

    fun saveUserToChatList(currentUserId:String,otherUserUserId:String)=viewModelScope.launch {
        chatDatabaseRepository.saveUserChatList(currentUserId,otherUserUserId)
            .onStart {
                _saveUserToChatListResponseState.value=RequestState.Loading
            }
            .catch {
                _saveUserToChatListResponseState.value=RequestState.Error(it)
            }
            .collect {
                Log.i("Userr", "saveUserToChatList: ${it.data}")
                _saveUserToChatListResponseState.value = RequestState.Success(it.data)
            }
    }
    fun resetSaveToChatListSuccessToIdle(){
        _saveUserToChatListResponseState.value=RequestState.Idle
    }

    //-------------------------------------------------------------------------------------------------------//

    private val _userProfileResponse= MutableStateFlow<RequestState<User>>(RequestState.Idle)
    val userProfileResponse: StateFlow<RequestState<User>>  = _userProfileResponse

    fun getUserData(userID: String)=viewModelScope.launch{
        _userProfileResponse.value=RequestState.Loading
        profileRepository.getUserData(userID)
            .onStart {
                _userProfileResponse.value=RequestState.Loading
            }
            .catch {
                _userProfileResponse.value=RequestState.Error(it)
            }
            .collect{
                _userProfileResponse.value=RequestState.Success(it.data)
            }
    }

    fun deleteAccount(userID: String,onSuccess: () -> Unit)=viewModelScope.launch{
        profileRepository.deleteAccount(userID)
            .onStart {

            }
            .catch {

            }
            .collect{
                storeLoginState.saveLoginState(false)
                storeLoginState.saveUserToken("")
                onSuccess()
            }
    }
    //-------------------------------------------------------------------------------------------------------//

    private val _userDetailsUpdateResponse = MutableStateFlow<RequestState<User>>(RequestState.Idle)
    val userDetailsUpdateResponse: StateFlow<RequestState<User>> = _userDetailsUpdateResponse

    fun updateUserDetails(id: String, backgroundImage: String) = viewModelScope.launch {
        val updateData = mapOf("backgroundImage" to backgroundImage)

        profileRepository.updateUserData(id, updateData)
            .onStart {
                _userDetailsUpdateResponse.value = RequestState.Loading
            }
            .catch {
                _userDetailsUpdateResponse.value = RequestState.Error(it)
            }
            .collect { result ->
                 val updated=UserObject.user.value.copy(backgroundImage = backgroundImage)
                storeUserState.saveUserInDataStore(updated)
                _userDetailsUpdateResponse.value = RequestState.Success(result.data)
            }
    }


}