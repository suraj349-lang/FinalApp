package com.example.finalapp.locationHelper

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.example.finalapp.utils.UserLocation
import com.example.finalapp.utils.constants.Constants
import java.io.IOException
import java.util.Locale


fun getReadableLocation(latitude: Double, longitude: Double, context: Context): String {
    var addressText = ""
    val geocoder = Geocoder(context, Locale.getDefault())

    try {

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            UserLocation.latitude=latitude
            UserLocation.longitude=longitude
            UserLocation.address= address.getAddressLine(0)
            UserLocation.city =address.locality ?: ""
            UserLocation.state=address.adminArea ?: ""
            UserLocation.country=address.countryName ?: ""
            UserLocation.district=address.subLocality?: ""
            UserLocation.street=address.thoroughfare ?: ""
            UserLocation.countryCode=address.countryCode ?: ""
            UserLocation.pinCode=address.postalCode ?: ""
            UserLocation.landmark=address.featureName

            addressText = "${address.getAddressLine(0)}, ${address.locality}"
        }

    } catch (e: IOException) {
        Log.d(Constants.TAG, e.message.toString())

    }

    return addressText

}


