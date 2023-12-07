package com.example.finalapp.offer

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.apiState.LoginApiState
import com.example.finalapp.apiState.OfferApiState
import com.example.finalapp.auth.repository.AuthRepository
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.OfferModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class OfferViewModel @Inject constructor(private val repository: OfferRepository): ViewModel() {
    val offerResponse:MutableState<OfferApiState> = mutableStateOf(OfferApiState.Empty)
    var key :MutableState<Int> = mutableStateOf(0);
    fun createOffer(offerData:OfferModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.sendCreateOfferData(offerData)
                .onStart {
                    offerResponse.value=OfferApiState.Loading;
                    Log.d("Data received",offerResponse.value.toString())
                }
                .catch {
                    Log.d("Data received","error found")
                    offerResponse.value=OfferApiState.Failure(it)
                    Log.d("Data received",offerResponse.value.toString())
                 }
                .collect {
                    offerResponse.value = OfferApiState.Success(it);
                    Log.d("Data received",offerResponse.value.toString())
                }
    }


}