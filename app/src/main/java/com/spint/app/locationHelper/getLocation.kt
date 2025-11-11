package com.spint.app.locationHelper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.AuthViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getLocation(context: Context, authViewModel: AuthViewModel){
    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
    ){
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.POST_NOTIFICATIONS),100)
        return
    }

    val location=fusedLocationProviderClient.getCurrentLocation(100,null)
    location.addOnSuccessListener {
        if(it!=null){
            authViewModel.latitude.value=it.latitude
            authViewModel.longitude.value=it.longitude
            authViewModel.address.value= getReadableLocation(authViewModel.latitude.value,authViewModel.longitude.value,context)
            Log.d("Flash Location", "getLocation: ${authViewModel.address.value}")
        }else{
            Log.d(Constants.TAG,"error fetching location")
        }
    }
}
