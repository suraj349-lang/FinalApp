package com.example.finalapp.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponseModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.SingleOfferModel
import com.example.finalapp.model.User
import com.example.finalapp.repository.OfferRepository
import com.example.finalapp.utils.RequestState
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class EventsViewModel @Inject constructor(private val offerRepository: OfferRepository, @ApplicationContext context: Context): ViewModel(){

    //-------------------------------------------DROP PROFILE--------------------------------------------------------------------------------------//

    val dropProfileResponse:MutableState<RequestState<DropProfileResponseModel>> = mutableStateOf(RequestState.Idle)
    fun dropProfile(data:DropProfileModel)=viewModelScope.launch(Dispatchers.IO) {
        offerRepository.sendDropProfileData(data)
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
    fun getDropProfile()=viewModelScope.launch(Dispatchers.IO) {
        offerRepository.getDropProfileData()
            .onStart {
                getDropProfileResponse.value=RequestState.Loading;
                Log.d("Data received",offerResponse.value.toString())
            }
            .catch {
                Log.d("Data received","error found")
                getDropProfileResponse.value=RequestState.Error(it)
                Log.d("Data received",offerResponse.value.toString())
            }
            .collect {
                getDropProfileResponse.value = RequestState.Success(it.data);
                Log.d("Data received",offerResponse.value.toString())
            }
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------//



    val offerResponse:MutableState<RequestState<SingleOfferModel>> = mutableStateOf(RequestState.Idle)
    var key :MutableState<Int> = mutableStateOf(0);
    fun createEvent(offerData:OfferModel)=viewModelScope.launch(Dispatchers.IO) {
        offerRepository.sendCreateEventData(offerData)
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
     private val placesClient = Places.createClient( context)

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
    var offersList= mutableStateOf<List<OfferModel>>(emptyList())

    val allOffers: MutableState<RequestState<List<OfferModel>>> = mutableStateOf(RequestState.Idle)

    fun getAllOffers()=viewModelScope.launch(Dispatchers.IO) {
        offerRepository.getAllOffers()
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





}