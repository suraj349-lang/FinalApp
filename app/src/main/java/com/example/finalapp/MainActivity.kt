package com.example.finalapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalapp.navigation.Navigation
import com.example.finalapp.ui.theme.FinalAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var mainViewModel= MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalApp{ getLocation() }

        }
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
    }
    private fun getLocation(){
        // check location permission
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100)
            return
        }
        //get latitude and longitude
        val location=fusedLocationProviderClient.getCurrentLocation(100,null)
        location.addOnSuccessListener {
            if(it!=null){
                mainViewModel.updateCoordinates(LatLng(it.latitude,it.longitude))
                Log.d("Coordinates from VM", mainViewModel.coordinates.value.latitude.toString())
                mainViewModel.updateAddress(getReadableLocation(it.latitude,it.longitude,this@MainActivity))
                Log.d("Coordinate", if(mainViewModel.address.value != "") mainViewModel.address.value else "No data")

                Log.d("Coordinates",it.latitude.toString()+"   "+ it.longitude.toString())
            }else{
                Log.d("Coordinates","error fetching location")
            }
        }
    }


}
@Composable
fun FinalApp(getLocation:()-> Unit) {
    val scope= rememberCoroutineScope()
    LaunchedEffect(key1 = true ){
        scope.launch(Dispatchers.IO) {
            getLocation()
        }

    }


    FinalAppTheme() {
        // A surface container using the 'background' color from the theme

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Navigation()

            }

        }
    }
}

data class LatLng(
    val latitude:Double,
    val longitude:Double
)
fun getReadableLocation(latitude: Double, longitude: Double, context: Context): String {
    var addressText = ""
    val geocoder = Geocoder(context, Locale.getDefault())

    try {

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            addressText = "${address.getAddressLine(0)}, ${address.locality}"
            // Use the addressText in your app
            Log.d("geolocation", addressText)
        }

    } catch (e: IOException) {
        Log.d("geolocation", e.message.toString())

    }

    return addressText

}