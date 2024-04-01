package com.example.finalapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.navigation.Navigation
import com.example.finalapp.permissions.LocationPermissionTextProvider
import com.example.finalapp.permissions.MainViewModel
import com.example.finalapp.permissions.NotificationPermissionTextProvider
import com.example.finalapp.permissions.PermissionDialog
import com.example.finalapp.permissions.PermissionsUI
import com.example.finalapp.ui.theme.FinalAppTheme
import com.example.finalapp.utils.Constants.Constants.TAG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.IOException
import java.util.Locale
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.finalapp.screens.onboarding.onboarding2.viewmodel.SplashViewModel
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS
    )
    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }

        setContent {
            FinalAppTheme {
                val authViewModel= hiltViewModel<AuthViewModel>()
                val locationSettingsLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    // Handle the activity result here
                }
                LaunchedEffect(key1 = true){

                    enableLocationSettings(this@MainActivity, launcher = locationSettingsLauncher, authViewModel)
                }

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val screen by splashViewModel.startDestination
                    FinalApp(authViewModel,screen) { getLocation(this, authViewModel) }
                } else {
                    PermissionsUI(onGoToAppSettingsClick = ::openAppSettings)
                    val permissionViewModel = viewModel<MainViewModel>()
                    val dialogQueue = permissionViewModel.visiblePermissionDialogQueue
                    val run by remember { mutableStateOf(0) }


                    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions(),
                        onResult = { perms ->
                            permissionsToRequest.forEach { permission ->
                                permissionViewModel.onPermissionResult(
                                    permission = permission,
                                    isGranted = perms[permission] == true
                                )
                            }
                        }
                    )
                    LaunchedEffect(key1 = run) {
                        multiplePermissionResultLauncher.launch(permissionsToRequest)
                    }
                    dialogQueue
                        .reversed()
                        .forEach { permission ->
                            PermissionDialog(
                                permissionTextProvider = when (permission) {
                                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                                        LocationPermissionTextProvider()
                                    }

                                    Manifest.permission.POST_NOTIFICATIONS -> {
                                        NotificationPermissionTextProvider()
                                    }

                                    else -> return@forEach
                                },
                                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                    permission
                                ),
                                onDismiss = permissionViewModel::dismissDialog,
                                onOkClick = {
                                    permissionViewModel.dismissDialog()
                                    multiplePermissionResultLauncher.launch(
                                        arrayOf(permission)
                                    )
                                },
                                onGoToAppSettingsClick = ::openAppSettings
                            )
                        }


                }
            }
        }
    }

}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}



@Composable
fun FinalApp(
    authViewModel: AuthViewModel,
    screen: String,
    getLocation: () -> Unit
) {
    val scope= rememberCoroutineScope()
    val value by remember{ mutableStateOf(false) }
    LaunchedEffect(value ){
        scope.launch(Dispatchers.IO) {
             getLocation()
             authViewModel.getProfileData()
        }

    }
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally)
        {
            Navigation(authViewModel,screen)
        }
    }
}




@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun enableLocationSettings(context: Context, launcher: ActivityResultLauncher<Intent>,authViewModel: AuthViewModel) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        // GPS is not enabled, prompt the user to enable it
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        launcher.launch(intent)
    } else {
        // GPS is already enabled
        // Handle this case if needed
      //  getLocation(context,authViewModel )

    }
}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun getLocation(context: Context, authViewModel:AuthViewModel){
    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    // check location permission
    if(ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        &&
        ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
    ){
        ActivityCompat.requestPermissions(
            context as Activity,
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
            authViewModel.address.value= getReadableLocation(authViewModel.latitude.value,authViewModel.longitude.value,context)
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
            Log.d(TAG,"error fetching location")
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
            Log.d(TAG, addressText)
            val addressComponents = addressText.split(", ")

            // Check if there are enough components
            if (addressComponents.size >= 2) {
                // The state is the second component
                Log.d(TAG,addressComponents[1])
            }
        }

    } catch (e: IOException) {
        Log.d(TAG, e.message.toString())

    }

    return addressText

}


@Serializable
data class User(val token: String, val address: String)
/*
//
//            try {
//                mSocket= IO.socket(Constants.BASE_URL)
//            }catch (e:Exception){
//                Log.d("Error in socket",e.message.toString())
//            }
//
//            mSocket.connect()
//            mSocket.emit("user",Constants.APP_NAME)
//            mSocket.emit("message","hello from ${Constants.APP_NAME}")
 */


