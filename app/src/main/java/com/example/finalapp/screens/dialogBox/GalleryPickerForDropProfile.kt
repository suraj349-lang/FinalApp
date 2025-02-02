package com.example.finalapp.screens.dialogBox

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import java.io.File
import java.io.FileOutputStream


//https://www.youtube.com/watch?v=uHX5NB6wHao
@Composable
fun GalleryPickerForDropProfile(navController: NavHostController, onFileCreated:(File)->Unit,onImageSelected:(Uri)->Unit) {
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val  context= LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri!=null) {
                Log.d("S3 Upload", "GalleryPickerForDropProfile: Image selected")
                selectedImageUri = uri
                onImageSelected(selectedImageUri!!)
                val imageFile = uriToFile(uri, context )
                onFileCreated(imageFile)
                Log.d("S3 Upload", "uri of image:${selectedImageUri} ")
            } else {
                // Navigate back if no image is selected
                navController.navigateUp()
            }
        }
    )
    LaunchedEffect(key1 = true) {
        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    BackHandler(true) {
    }
}
// Function to convert URI to File
fun uriToFile(uri: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val tempFile = File(context.cacheDir, "uploaded_image.jpg")

    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }

    return tempFile
}