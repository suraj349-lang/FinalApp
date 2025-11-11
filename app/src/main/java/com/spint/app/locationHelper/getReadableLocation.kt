package com.spint.app.locationHelper

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.spint.app.utils.UserLocation
import com.spint.app.utils.UserLocationObject
import com.spint.app.utils.constants.Constants
import java.io.IOException
import java.util.Locale


fun getReadableLocation(latitude: Double, longitude: Double, context: Context): String {
    var addressText = ""
    val geocoder = Geocoder(context, Locale.getDefault())

    try {

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            UserLocationObject.updateLocation(
                UserLocation(
                    latitude = latitude,
                    longitude = longitude,
                    address = address.getAddressLine(0),
                    city = address.locality ?: "",
                    state = address.adminArea ?: "",
                    country = address.countryName ?: "",
                    district = address.subLocality ?: "",
                    street = address.thoroughfare ?: "",
                    countryCode = address.countryCode ?: "",
                    pinCode = address.postalCode ?: "",
                    landmark = address.featureName
                )
            )


            addressText = "${address.getAddressLine(0)}, ${address.locality}"
        }

    } catch (e: IOException) {
        Log.d(Constants.TAG, e.message.toString())

    }

    return addressText

}


