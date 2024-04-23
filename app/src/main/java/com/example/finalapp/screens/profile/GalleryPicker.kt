package com.example.finalapp.screens.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.finalapp.utils.Constants.Constants
import com.example.finalapp.utils.RequestState


//https://www.youtube.com/watch?v=uHX5NB6wHao
@Composable
fun GalleryPicker(navController: NavHostController,profileViewModel: ProfileViewModel) {

    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val context= LocalContext.current
    // Photo picker launcher
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                selectedImageUris = uris
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
        navController.navigateUp()
    }
    when (val result=profileViewModel.imageUploadResponse.value){
        is RequestState.Idle->{
            CircularProgressIndicator()
        }
        is RequestState.Loading -> {
            CircularProgressIndicator()
        }
        is RequestState.Success ->{
            Toast.makeText(context,result.data.toString(), Toast.LENGTH_SHORT).show()

        }
        is RequestState.Error ->{
            Toast.makeText(context,result.error.message.toString(), Toast.LENGTH_SHORT).show()
            Log.d(Constants.TAG, "ImageCaptureFromCamera: ${result.error.message}")

        }

        else -> {}
    }
    if (selectedImageUris.isNotEmpty()) {
        profileViewModel.uploadImage(selectedImageUris[0], context )

        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(selectedImageUris) { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight(0.4f),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
