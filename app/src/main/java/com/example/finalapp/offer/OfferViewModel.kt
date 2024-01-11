package com.example.finalapp.offer

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.apiState.OfferApiState
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.utils.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class OfferViewModel @Inject constructor(private val repository: OfferRepository): ViewModel() {
    val offerResponse:MutableState<RequestState<OfferResponseModel>> = mutableStateOf(RequestState.Idle)
    var key :MutableState<Int> = mutableStateOf(0);
    fun createOffer(offerData:OfferModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.sendCreateOfferData(offerData)
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


}