package com.example.finalapp.screens._4profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.finalapp.utils.constants.Constants.TAG
import com.example.finalapp.utils.RequestState
import com.example.finalapp.viewmodels.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ImageCaptureFromCamera(profileViewModel: ProfileViewModel) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
       // "${context.packageName}.provider",// todo edited so as to change the image file name
        "vectorapp.provider",
        file
    )
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            capturedImageUri = uri
            profileViewModel.uploadUserImage(uri,"besetsuraj", context )

        } else {
            Toast.makeText(context, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (capturedImageUri != Uri.EMPTY) {
        Image(
            painter = rememberImagePainter(capturedImageUri),
            contentDescription = "Captured Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        when (val result=profileViewModel.imageUploadResponse.value){
            is RequestState.Idle->{
                CircularProgressIndicator()
            }
            is RequestState.Loading -> {
                CircularProgressIndicator()
            }
            is RequestState.Success ->{
                Toast.makeText(context,result.data.toString(),Toast.LENGTH_SHORT).show()

            }
            is RequestState.Error ->{
                Toast.makeText(context,result.error.message.toString(),Toast.LENGTH_SHORT).show()
                Log.d(TAG, "ImageCaptureFromCamera: ${result.error.message}")

            }

            else -> {}
        }
    } else {
        // Display placeholder or loading state if needed
    }
}



fun uriToMultipart(uri: Uri, context: Context): MultipartBody.Part {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val byteArray = inputStream?.readBytes() ?: ByteArray(0)
    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("image", "filename.jpg", requestBody)
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    val storageDir = externalCacheDir // Get external cache directory
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}


//@Composable
//fun ImageCaptureFromCamera(profileViewModel: ProfileViewModel) {
//    val context = LocalContext.current
//    val file = context.createImageFile()
//    if (!file.exists()) {
//        Log.e(TAG, "Failed to create image file at: ${file.absolutePath}")
//        return // Handle the error or return an error state
//    }
//
//    val uri = FileProvider.getUriForFile(
//        context,
//        context.packageName + ".provider",
//        file
//    )
//
//
//    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
//    // Camera Launcher
//    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            capturedImageUri = uri
//        } else {
//            Toast.makeText(context, "Image capture failed", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Permission Launcher
//    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
//        if (granted) {
//            cameraLauncher.launch(uri)
//        } else {
//            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//
//    // Launching permission and camera based on current permissions
//    LaunchedEffect(key1 = Unit) {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            cameraLauncher.launch(uri)
//        } else {
//            permissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }
//    if (capturedImageUri == Uri.EMPTY) {
//        Log.e(TAG, "No image captured")
//        // Handle the error or return an error state
//    }
//
//
//    Surface(modifier = Modifier.fillMaxSize()) {
//        if (capturedImageUri != Uri.EMPTY) {
//            Image(
//                painter = rememberImagePainter(capturedImageUri),
//                contentDescription = "Captured Image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
//            if (uri.toString() != file.toURI().toString()) {
//                Log.e(TAG, "Mismatch between URI and file path")
//                return@Surface // Handle the error or return an error state
//            }
//            // Update ViewModel
//            profileViewModel.imageUri.value = capturedImageUri
//            LaunchedEffect(key1 = capturedImageUri) {
//                profileViewModel.uploadImage(capturedImageUri)
//            }
//        } else {
//            // Handle scenario where no image is captured yet
//        }
//        when (val result=profileViewModel.imageUploadResponse.value){
//            is RequestState.Idle->{
//                CircularProgressIndicator()
//            }
//            is RequestState.Loading -> {
//                CircularProgressIndicator()
//            }
//            is RequestState.Success ->{
//                Toast.makeText(context,result.data.toString(),Toast.LENGTH_SHORT).show()
//
//            }
//            is RequestState.Error ->{
//                Toast.makeText(context,result.error.message.toString(),Toast.LENGTH_SHORT).show()
//                Log.d(TAG, "ImageCaptureFromCamera: ${result.error.message}")
//
//            }
//
//            else -> {}
//        }
//    }
//
//
//}


