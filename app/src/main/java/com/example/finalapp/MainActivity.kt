package com.example.finalapp

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.navigation.Navigation
import com.example.finalapp.permissions.LocationPermissionTextProvider
import com.example.finalapp.permissions.MainViewModel
import com.example.finalapp.permissions.NotificationPermissionTextProvider
import com.example.finalapp.permissions.PermissionDialog
import com.example.finalapp.permissions.PermissionsUI
import com.example.finalapp.ui.theme.FinalAppTheme
import com.example.finalapp.utils.constants.Constants.TAG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.finalapp.datastore.StoreUserState
import com.example.finalapp.locationHelper.enableLocationSettings
import com.example.finalapp.locationHelper.getLocation
import com.example.finalapp.viewmodels.SplashViewModel
import com.example.finalapp.ui.API_KEY
import com.example.finalapp.utils.UserLocation
import com.google.android.libraries.places.api.Places
import io.socket.client.Socket
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
     @Inject
     lateinit var storeUserState:StoreUserState

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        handleDeepLink(intent)
        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }
        Places.initialize(applicationContext, API_KEY)
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

                if (authViewModel.permission.value) {
                    splashViewModel.startDestination.value?.let { screen ->
                        if (!splashViewModel.isLoading.value) {
                            FinalApp(authViewModel, screen) { getLocation(this, authViewModel) }
                        }
                    }

                } else {
                    PermissionsUI(authViewModel,onGoToAppSettingsClick = ::openAppSettings)

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.let { handleDeepLink(it) }
    }

    private fun handleDeepLink(intent: Intent) {
        val data = intent.data
        data?.let {
            val path = it.path
            val queryParams = it.query
            val id = it.getQueryParameter("id")
            Log.d("DeepLink", "Path: $path, ID: $id")
            // Navigate or take action based on the data
        }
    }

}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinalApp(
    authViewModel: AuthViewModel,
    screen: String,
    getLocation: () -> Unit
) {
    val scope= rememberCoroutineScope()
    LaunchedEffect(Unit){
        scope.launch(Dispatchers.IO) {
             getLocation()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally)
        {
            Navigation(authViewModel,screen)
        }
    }
}
fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}


/////------------------------COMMENTED CODE--------------------------------------------------------------------
/////----------------------------------------------------------------------------------------------------------
/////----------------------------------------------------------------------------------------------------------

/*
//        try {
//            mSocket = IO.socket(Constants.TEMP_SOCKET_URL)
//            mSocket?.connect()
//            mSocket?.on(Socket.EVENT_CONNECT) {
//                Log.d("SocketIO", "Connected to server")
//                mSocket?.emit("user", Constants.APP_NAME)
//                mSocket?.emit("message", "hello from ${Constants.APP_NAME}")
//            }
//            mSocket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
//                Log.d("SocketIO", "Connection error: ${args[0]}")
//            }
//        } catch (e: Exception) {
//            Log.d("Error in socket", e.message.toString())
//        }


 */


/*
private fun getCity(context: Context, authViewModel:AuthViewModel,activity: Activity){
    //location
     lateinit var locationCallback:LocationCallback


    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000000000).build()
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val newPos = LatLng(locationResult.lastLocation?.latitude!!, locationResult.lastLocation?.longitude!!)
            authViewModel.currentLocation.value= LatLng(locationResult.lastLocation?.latitude!!,locationResult.lastLocation?.longitude!!)
            Log.d("Location compose", "onLocationResult:$newPos ")

            val geocoder= Geocoder(context, Locale.getDefault())
            val addressList: List<Address>?
            try {
                addressList=geocoder.getFromLocation(
                    locationResult.lastLocation?.latitude!!,
                    locationResult.lastLocation?.longitude!!,
                    1
                )
                val cityName=addressList?.get(0)!!.locality
                authViewModel.city.value=cityName
                Log.d("Location compose",  authViewModel.city.value)


            }catch (e: IOException){
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()

            }

        }
    }


    var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
        return
    }
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

}
*/