package com.spint.app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.navigation.Navigation
import com.spint.app.permissions.LocationPermissionTextProvider
import com.spint.app.permissions.MainViewModel
import com.spint.app.permissions.NotificationPermissionTextProvider
import com.spint.app.permissions.PermissionDialog
import com.spint.app.permissions.PermissionsUI
import com.spint.app.ui.theme.FinalAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.spint.app.datastore.StoreUserState
import com.spint.app.locationHelper.enableLocationSettings
import com.spint.app.locationHelper.getLocation
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._8notification.pushNotificationService.createNotificationChannels
import com.spint.app.viewmodels.SplashViewModel
import com.spint.app.ui.API_KEY
import com.spint.app.viewmodels.ChatViewModel
import com.google.android.libraries.places.api.Places
import com.spint.app.datastore.StoreLoginState
import kotlinx.coroutines.flow.first
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS
    )
    private val authViewModel:AuthViewModel by viewModels()
    private val chatViewModel:ChatViewModel by viewModels()
    @Inject
    lateinit var splashViewModel: SplashViewModel
    @Inject
    lateinit var storeUserState:StoreUserState
    @Inject
    lateinit var loginState: StoreLoginState


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        createNotificationChannels(this)
        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }
        Places.initialize(applicationContext, API_KEY)
     //   chatViewModel.connectSocket()
        setContent {
            FinalAppTheme {
                val locationSettingsLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    // Handle the activity result here
                }

                LaunchedEffect(key1 = true){
                    enableLocationSettings(this@MainActivity, launcher = locationSettingsLauncher, authViewModel)
                }
                val navController = rememberNavController()

                // Listen for new deep links emitted by ViewModel
                LaunchedEffect(Unit) {
                    authViewModel.deepLinkUri.collect { uri ->
                        handleDeepLink(uri, navController)
                    }
                }

                // Handle deep link if app opened directly by link
                LaunchedEffect(Unit) {
                    handleDeepLink(intent?.data, navController)
                }
                var isLoggedIn by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    isLoggedIn=loginState.getLoginState.first() ?: false
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
        authViewModel.sendDeepLink(intent?.data)
    }

    override fun onDestroy() {
        super.onDestroy()
        chatViewModel.disconnectSocket()
    }

}
fun handleDeepLink(uri: Uri?, navController: NavHostController) {
    uri ?: return
    val pathSegments = uri.pathSegments

    if (pathSegments.isEmpty()) return

    when (pathSegments.firstOrNull()) {
        "chat" -> navController.navigate("chat/${pathSegments.lastOrNull()}")
        "ping" -> navController.navigate(SCREENS.FLASH_POSTS.route)
        "profile" -> navController.navigate("profile/${pathSegments.lastOrNull()}")
        "qr" -> navController.navigate("qr/${pathSegments.lastOrNull()}")
    }
}





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