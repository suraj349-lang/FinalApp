package com.spint.app.utils

import com.spint.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserObject {
    private val _user = MutableStateFlow(User())
    var user: StateFlow<User> = _user

    fun updateUser(newUser: User) {
        _user.value = newUser
    }
}



object TokenObject{
    var token:String=""
    var currentLocation:String=""
}