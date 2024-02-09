package com.example.finalapp


import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class MainViewModel :ViewModel(){


    private val _coordinates = MutableStateFlow(LatLng(0.0,0.0))
    val coordinates: StateFlow<LatLng> = _coordinates

    fun updateCoordinates(value:LatLng) {
        _coordinates.value =value
    }

    private val _address = MutableStateFlow<String>("")
    val address: StateFlow<String> = _address

    fun updateAddress(value:String) {
        _address.value =value
    }

    override fun onCleared() {
        Log.d("Coordinates", "onCleared:MainViewmodel ")
    }






}