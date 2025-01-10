package com.example.finalapp.screens.dialogBox

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.finalapp.viewmodels.ProfileViewModel
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants.TAG
import kotlinx.coroutines.launch


//https://www.youtube.com/watch?v=uHX5NB6wHao
@Composable
fun GalleryPickerForDropProfile(navController: NavHostController, onImageSelected:(Uri)->Unit) {

    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val lifecycleOwner= LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
               // navController.navigate(SCREENS.PROFILE.route)

            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val context= LocalContext.current
    // Photo picker launcher
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                Log.d("Suraj", "GalleryPickerForDropProfile: Image selected")
                selectedImageUris = uris
                onImageSelected(selectedImageUris[0])
                Log.d("Suraj", "uri of image:${selectedImageUris[0]} ")
            } else {
                // Navigate back if no image is selected
                navController.navigateUp()
            }
        }
    )
    LaunchedEffect(key1 = true) {
        multiplePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    BackHandler(true) {
       // navController.navigateUp()
    }

//    if (selectedImageUris.isNotEmpty()) {
//        val scope= rememberCoroutineScope()
//        LaunchedEffect(key1 = true) {
//                profileViewModel.uploadImage(selectedImageUris[0], context)
//
//        }
//
//    }
}
