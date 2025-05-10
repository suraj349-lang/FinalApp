package com.example.finalapp.locationHelper

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import com.example.finalapp.viewmodels.AuthViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun enableLocationSettings(context: Context, launcher: ActivityResultLauncher<Intent>, authViewModel: AuthViewModel) {
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