package com.example.finalapp.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalapp.model.ChatList
import com.example.finalapp.model.DirectChat
import com.example.finalapp.model.DirectChatRequest
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.model.EventRequestDTO
import com.example.finalapp.model.EventResponse
import com.example.finalapp.model.EventResponseDTO
import com.example.finalapp.model.GetDropProfileResponseModel
import com.example.finalapp.model.PremiumEventResponseDTO
import com.example.finalapp.model.User
import com.example.finalapp.model.pings.PingRequestDto
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.paging.DirectChatUsersPagingSource
import com.example.finalapp.paging.DropProfilePagingSource
import com.example.finalapp.repository.ChatDatabaseRepository
import com.example.finalapp.repository.EventsRepository
import com.example.finalapp.repository.ProfileRepository
import com.example.finalapp.repository.Resource
import com.example.finalapp.screens._2pings.PingsPagingSource
import com.example.finalapp.utils.RequestState
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File

import javax.inject.Inject
import kotlin.Exception


@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val chatDatabaseRepository: ChatDatabaseRepository,
    private val profileRepository: ProfileRepository,
    private val s3Uploader: S3Uploader,
    @ApplicationContext context: Context): ViewModel(){

    private val placesClient by lazy { Places.createClient(context) }
    var checked= mutableStateOf(false)
    var shareProfileClicked= mutableStateOf(false)
    init {
        viewModelScope.launch(Dispatchers.Main) {
            val job = viewModelScope.launch {
                getAllEvents()
            }
            job.join()
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
                loadDirectChatUsers(data.lat, data.long)
            }
    }

    fun loadDirectChatUsers(lat: Double, long: Double) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 10, prefetchDistance = 5),
                pagingSourceFactory = { DirectChatUsersPagingSource(eventsRepository, lat, long) }
            ).flow
              //  .cachedIn(viewModelScope)
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

//-----------------------------------------------------------------------------------------------------------------------------------------------//


    fun getAutocompletePredictions(query: String): Flow<List<AutocompletePrediction>> {
        val results = MutableStateFlow<List<AutocompletePrediction>>(emptyList())

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                results.value = response.autocompletePredictions
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesViewModel", "Autocomplete prediction request failed: ${exception.message}")
            }

        return results
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
    private val _userPingsListResponse = MutableStateFlow<RequestState<List<PingResponse>>>(RequestState.Idle)
    val userPingsListResponse: StateFlow<RequestState<List<PingResponse>>> = _userPingsListResponse.asStateFlow()
    val canFetchPings = mutableStateOf(true)

    fun getUserPings(id:String)=viewModelScope.launch(Dispatchers.IO) {
        val TAG="GET_Pings_RESPONSE";
        eventsRepository.getUserPings(id)
            .onStart {
                _userPingsListResponse.value = RequestState.Loading

            }.catch {
                _userPingsListResponse.value = RequestState.Error(it)
                Log.d(TAG, "user events error ${_userPingsListResponse.value}")

            }.collect {
                _userPingsListResponse.value = RequestState.Success(it.data)
                canFetchPings.value=false
                Log.d(TAG, "user events data ${_userPingsListResponse.value}")

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

    var createEventResponse = MutableStateFlow(EventResponseDTO())
    var createEventIsLoading = MutableStateFlow(false)
    var createEventIsSuccess= MutableStateFlow(false)
    fun createEvent(data:EventRequestDTO){
        createEventIsLoading.value=true
        viewModelScope.launch(Dispatchers.IO) {
//            if(data.location=="") return@launch
            try {
                when(val response=eventsRepository.createEvent(data)){
                    is Resource.Success->{
                        createEventResponse.value= response.data!!
                        createEventIsSuccess.value=true

                    }
                    is Resource.Error->{
                        Log.d("TAG","create event ${response.message.toString()}")
                    }
                    else->{
                     }
                }
            }catch (e:Exception){
                Log.d("TAG","create event ${e.message.toString()}")
            }
            finally {
                createEventIsLoading.value=false
            }

        }

    }



//----------------------------------------------------------------------------------------------------------------------------------//

    val premiumCreateEventResponse:MutableState<RequestState<PremiumEventResponseDTO>> = mutableStateOf(RequestState.Idle)
    var premiumCreateEventKey :MutableState<Int> = mutableStateOf(0);
    fun premiumCreateEvent(event:EventRequestDTO)=viewModelScope.launch(Dispatchers.IO) {
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

    private var _createPingResponse:MutableStateFlow<RequestState<String>> = MutableStateFlow(RequestState.Idle)
    var createPingResponse :StateFlow<RequestState<String>> = _createPingResponse

    fun createPing(ping:PingRequestDto)=viewModelScope.launch(Dispatchers.IO) {
        val tag="CREATE_PING_RESPONSE"

        eventsRepository.createPing(ping)
            .onStart {
                _createPingResponse.value=RequestState.Loading;

                Log.d(tag,_createPingResponse.value.toString())
            }
            .catch {

                Log.e(tag,it.printStackTrace().toString())
                _createPingResponse.value=RequestState.Error(it)
                Log.e(tag,it.message.toString())
            }
            .collect {
                if(it.success.uppercase() =="TRUE") {
                    Log.i(tag, "createPing: success")
                    createEventIsSuccess.value=true
                    _createPingResponse.value = RequestState.Success(it.data);
                }else{
                    Log.e(tag, it.message)
                   // _createPingResponse.value = RequestState.Error(it);
                }

            }
    }

//--------------------------------------------------------------------------------------------------------------------//

    private val _allPingsFlow = MutableStateFlow<Flow<PagingData<PingResponse>>?>(null)
    val allPingsFlow: StateFlow<Flow<PagingData<PingResponse>>?> = _allPingsFlow.asStateFlow()
//    private val _shouldLoadDroppedProfiles= MutableStateFlow(false)
//    val shouldLoadDroppedProfiles:StateFlow<Boolean>  = _shouldLoadDroppedProfiles

    fun getAllPings(location:String) {
        _allPingsFlow.value = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 5),
            pagingSourceFactory = { PingsPagingSource(eventsRepository) }
        ).flow.cachedIn(viewModelScope)
    }



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

}