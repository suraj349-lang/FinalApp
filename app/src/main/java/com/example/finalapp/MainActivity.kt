package com.example.finalapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.navigation.Navigation
import com.example.finalapp.ui.theme.FinalAppTheme
import com.example.finalapp.utils.Constants.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import java.io.IOException
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var mSocket:Socket
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var localToken= mutableStateOf("")

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel= hiltViewModel<AuthViewModel>()



            try {
                mSocket= IO.socket(Constants.BASE_URL)
            }catch (e:Exception){
                Log.d("Error in socket",e.message.toString())
            }

            mSocket.connect()
            mSocket.emit("user",Constants.APP_NAME)
            mSocket.emit("message","hello from ${Constants.APP_NAME}")
            FinalApp(mSocket,authViewModel){ getLocation(authViewModel) }

        }
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getLocation(authViewModel:AuthViewModel){
        // check location permission
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.POST_NOTIFICATIONS),100)
            return
        }

        //get latitude and longitude
        val location=fusedLocationProviderClient.getCurrentLocation(100,null)
        location.addOnSuccessListener {
            if(it!=null){

//                mSocket.emit("location",Constants.APP_NAME + "  "+it.latitude.toString() + " " + it.longitude.toString())
//                mSocket.on("location"){data->
//                    Log.d("Coordinates received from node", data[0].toString())
//                }
                authViewModel.latitude.value=it.latitude
                authViewModel.longitude.value=it.longitude
//                Log.d("Coordinates", authViewModel.latitude.value.toString() + "    "+authViewModel.longitude.value.toString())
                authViewModel.address.value= getReadableLocation(authViewModel.latitude.value,authViewModel.longitude.value,this@MainActivity)
//                Log.d("Coordinates", authViewModel.address.value)
//
//
//                val userTokenAndAddress=User(token = localToken.value, address = authViewModel.address.value)
//                mSocket.emit("address",Json.encodeToString(userTokenAndAddress))
//
//
//                mSocket.on("address"){data->
//                  Log.d("Coordinates changed to address", data[0].toString())
                   // Log.d("Coordinates changed to address","no address")
              //  }
            }else{
                Log.d("Coordinates","error fetching location")
            }
        }
    }


}
@Composable
fun FinalApp(mSocket: Socket, authViewModel: AuthViewModel, getLocation: () -> Unit) {
    val scope= rememberCoroutineScope()
    LaunchedEffect(key1 = true ){
        scope.launch(Dispatchers.IO) {
            getLocation()
            authViewModel.getProfileData()
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
                Navigation(authViewModel)

            }

        }
    }
}

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
            val addressComponents = addressText.split(", ")

            // Check if there are enough components
            if (addressComponents.size >= 2) {
                // The state is the second component
                Log.d("Coordinates state",addressComponents[1])
            }
        }

    } catch (e: IOException) {
        Log.d("geolocation", e.message.toString())

    }

    return addressText

}

@Serializable
data class User(val token: String, val address: String)

