package com.example.finalapp.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserLocationObject {
    private val _userLocation = MutableStateFlow(UserLocation())
    val userLocation: StateFlow<UserLocation> = _userLocation

    fun updateLocation(newLocation: UserLocation) {
        _userLocation.value = newLocation
    }

    // Optional: update individual fields
    fun updateLatitude(lat: Double?) {
        _userLocation.value = _userLocation.value.copy(latitude = lat)
    }

    // You can add similar functions for other fields if needed
}


data class UserLocation(
    var latitude:Double? =null,
    var longitude:Double? =null,
    var address :String ?=null,
    var street: String? =null,
    var city: String? =null,
    var district : String? =null,
    var state: String? =null,
    var country: String? =null,
    var pinCode: String? =null,
    var countryCode: String? =null,
    var landmark: String? =null,
)