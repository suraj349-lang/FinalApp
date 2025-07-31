package com.example.finalapp.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.finalapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserObject {
    private val _user = MutableStateFlow(User())
    var user: StateFlow<User> = _user

    fun updateUser(newUser: User) {
        _user.value = newUser
    }
    fun updateName(name: String) {
        _user.value = _user.value.copy(name = name)
    }
    fun updateUserName(username: String) {
        _user.value = _user.value.copy(username = username)
    }

    fun updateProfileImage(newImage: String) {
        _user.value = _user.value.copy(profileImage = newImage)
    }
    fun updateBackgroundImage(newImage: String) {
        _user.value = _user.value.copy(backgroundImage = newImage)
    }
}


object UserLocation{
    var latitude:Double? =null
    var longitude:Double? =null
    var address :String ?=null
    var street: String? =null
    var city: String? =null
    var district : String? =null
    var state: String? =null
    var country: String? =null
    var pinCode: String? =null
    var countryCode: String? =null
    var landmark: String? =null
}

object TokenObject{
    var token:String=""
    var currentLocation:String=""
}