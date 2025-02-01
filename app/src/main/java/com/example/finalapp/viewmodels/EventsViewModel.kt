package com.example.finalapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.model.DirectChat
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponseModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.SingleOfferModel
import com.example.finalapp.model.User
import com.example.finalapp.repository.EventsRepository
import com.example.finalapp.repository.Resource
import com.example.finalapp.utils.RequestState
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

import javax.inject.Inject
import kotlin.Exception


@HiltViewModel
class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository, @ApplicationContext context: Context): ViewModel(){
    private val placesClient by lazy { Places.createClient(context) }
    init {
        viewModelScope.launch(Dispatchers.Main) {
            val job = viewModelScope.launch {
                getAllEvents()
            }
            job.join()
        }
    }

    //-------------------------------------------DROP PROFILE--------------------------------------------------------------------------------------//

    val dropProfileResponse:MutableState<RequestState<DropProfileResponseModel>> = mutableStateOf(RequestState.Idle)
    fun dropProfile(data:DropProfileModel)=viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.sendDropProfileData(data)
            .onStart {
                dropProfileResponse.value=RequestState.Loading;
                Log.d("Data received",offerResponse.value.toString())
            }
            .catch {
                Log.d("Data received","error found")
                dropProfileResponse.value=RequestState.Error(it)
                Log.d("Data received",offerResponse.value.toString())
            }
            .collect {
                dropProfileResponse.value = RequestState.Success(it);
                Log.d("Data received",offerResponse.value.toString())
            }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------//
    val getDropProfileResponse:MutableState<RequestState<List<DropProfileModel>>> = mutableStateOf(RequestState.Idle)
    var droppedProfilesList= mutableStateOf<List<DropProfileModel>>(emptyList())
     suspend fun getAllDropProfiles(location:String="",date:String="") {
        eventsRepository.getAllDropProfiles()
            .onStart {
                getDropProfileResponse.value=RequestState.Loading;
                Log.d("Data received",offerResponse.value.toString())
            }
            .catch {
                Log.d("Data received","error found")
                getDropProfileResponse.value=RequestState.Error(it)
                Log.d("Data received",offerResponse.value.toString())
            }.collect {
                getDropProfileResponse.value = RequestState.Success(it.data);
                Log.d("Data received",offerResponse.value.toString())
            }
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------//



    val offerResponse:MutableState<RequestState<SingleOfferModel>> = mutableStateOf(RequestState.Idle)
    var key :MutableState<Int> = mutableStateOf(0);
    fun premiumCreateEvent(offerData:OfferModel)=viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.sendCreateEventData(offerData)
                .onStart {
                    offerResponse.value=RequestState.Loading;
                    Log.d("Data received",offerResponse.value.toString())
                }
                .catch {
                    Log.d("Data received","error found")
                    offerResponse.value=RequestState.Error(it)
                    Log.d("Data received",offerResponse.value.toString())
                 }
                .collect {
                    offerResponse.value = RequestState.Success(it);
                    Log.d("Data received",offerResponse.value.toString())
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
    //-------------------------------------------DIRECT CHAT--------------------------------------------------------------------------------------//

    fun shareChatFunction(data: DirectChat){
        viewModelScope.launch {
            sendDirectChatData(data)
            getNearByUsers(data.lat,data.long)
        }

    }
    val directChatResponse:MutableState<RequestState<DirectChat>> = mutableStateOf(RequestState.Idle)
    fun sendDirectChatData(data: DirectChat)=viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.setLocationForDirectChat(data)
            .onStart {
                directChatResponse.value=RequestState.Loading;
                Log.d("Data received",offerResponse.value.toString())
            }
            .catch {
                Log.d("Data received","error found")
                directChatResponse.value=RequestState.Error(it)
                Log.d("Data received",offerResponse.value.toString())
            }
            .collect {
                directChatResponse.value = RequestState.Success(it.data);
                Log.d("Data received",offerResponse.value.toString())
            }
    }

//--------------------------------------------------------------------------------------------------------------------//
    val nearByUserResponse:MutableState<RequestState<List<User>>> = mutableStateOf(RequestState.Idle)
    var nearByUsersList= mutableStateOf<List<User>>(emptyList())
    fun getNearByUsers(lat:Double, long: Double)=viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.getDirectChatUsers(lat,long)
            .onStart {
                nearByUserResponse.value=RequestState.Loading;
                Log.d("Data received",offerResponse.value.toString())
            }
            .catch {
                Log.d("Data received","error found")
                nearByUserResponse.value=RequestState.Error(it)
                Log.d("Data received",offerResponse.value.toString())
            }
            .collect {
                nearByUserResponse.value = RequestState.Success(it.data);
                Log.d("Data received",offerResponse.value.toString())
            }
    }



    //--------------------------------------------------------------------------------------------------------------------//
    var offersList= mutableStateOf<List<OfferModel>>(emptyList())

    val allOffers: MutableState<RequestState<List<OfferModel>>> = mutableStateOf(RequestState.Idle)

    fun getAllEvents()=viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.getAllEvents()
            .onStart {
                allOffers.value = RequestState.Loading
                Log.d("ZUNE", "all profiles start ${allOffers.value}")

            }.catch {
                allOffers.value = RequestState.Error(it)
                Log.d("ZUNE", "all profiles error ${allOffers.value}")

            }.collect {
                allOffers.value = RequestState.Success(it.data)
                Log.d("ZUNE", "all profiles data ${allOffers.value}")

            }
    }

   var createEventResponse = MutableStateFlow(SingleOfferModel(false,100, OfferModel("","","","","")))
    var isLoading = MutableStateFlow(false)
    var isSuccess= MutableStateFlow(false)
    fun createEvent(data:OfferModel){
        isLoading.value=true
        viewModelScope.launch(Dispatchers.IO) {
//            if(data.location=="") return@launch
            try {
                when(val response=eventsRepository.createEvent(data)){
                    is Resource.Success->{
                        createEventResponse.value= response.data!!
                        isSuccess.value=true;
                      //  isLoading.value=false

                    }
                    is Resource.Error->{
                        Log.d("TAG","create event ${response.message.toString()}")
                     //   isLoading.value=false
                    }
                    else->{//isLoading.value=false
                     }
                }
            }catch (e:Exception){
                Log.d("TAG","create event ${e.message.toString()}")
            }
            finally {
                isLoading.value=false
            }

        }

    }





}