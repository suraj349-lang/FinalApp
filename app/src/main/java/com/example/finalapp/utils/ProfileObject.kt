package com.example.finalapp.utils

import com.example.finalapp.database.Profile

object ProfileObject {
    var profile: Profile? = null
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