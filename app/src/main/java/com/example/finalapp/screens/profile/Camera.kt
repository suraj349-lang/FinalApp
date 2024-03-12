package com.example.finalapp.screens.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun ImageCaptureFromCamera() {
    val context = LocalContext.current
    val file = context.createImageFile();
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission())
        {
            if (it) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    Surface(modifier = Modifier.fillMaxSize()) {
//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(10.dp),
//        verticalArrangement = Arrangement.Bottom,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = {
        LaunchedEffect(key1 = true) {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)

            }
        }

//{
//            Text(text ="Capture Image")
//
//        }
//
//    }
        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier.padding(16.dp, 8.dp),
                painter = rememberImagePainter(capturedImageUri),
                // painter = painterResource(id = R.drawable.ic_image),
                contentDescription = ""
            )
            Log.d("suraj", capturedImageUri.toString())
            // saveImageToGallery(context,capturedImageUri)
            Log.d("suraj", capturedImageUri.toString())
        }else{
            Surface(Modifier.fillMaxSize()) {
                // Image(painter = painterResource(id = R.drawable.ic_image), contentDescription ="" )

            }
        }

    }
}

fun Context.createImageFile(): File {
    val timeStamp= SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName="JPEG_"+timeStamp+"_"
    val image= File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}