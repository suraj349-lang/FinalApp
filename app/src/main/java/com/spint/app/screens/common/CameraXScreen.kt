package com.spint.app.screens.common

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.spint.app.R
import com.spint.app.enums.ImageUploadScreens
import java.io.File


@Composable
fun CameraXScreen(navController: NavHostController, screen: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    var imageList by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var permissionsGranted by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraSide=if (screen != ImageUploadScreens.CREATE_EVENT.screen) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
    var cameraSelector by remember { mutableStateOf(cameraSide) }

    var isGalleryOpen by remember { mutableStateOf(false) }


    // State to track loading more images
    var isLoading by remember { mutableStateOf(false) }

    // Permission handling
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionsGranted = permissions.entries.all { it.value }
        if (!permissionsGranted) {
            Toast.makeText(context, "Permissions are required", Toast.LENGTH_LONG).show()
        }
    }
    //multiple picker
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetMultipleContents()
//    ) { uris ->
//        isGalleryOpen = false  // Reset the flag
//
//        if (uris.isNotEmpty()) {
//            imageList = uris
//            selectedImageUri = uris.first()
//            navController.navigate("preview/${Uri.encode(selectedImageUri.toString())}")
//        }
//    }
    // single picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        isGalleryOpen = false
        if (uri != null) {
            selectedImageUri = uri
            navController.navigate("preview/$screen/${Uri.encode(uri.toString())}")
        }
    }




    // Permissions array based on Android version
    val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_MEDIA_IMAGES // For newer API levels
        )
    } else {
        arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE // For older API levels
        )
    }



    // Function to load more images
    fun loadMoreImages(context: Context) {
        if (isLoading) return
        isLoading = true

        // Fetch recent images from gallery
        val newImages = getRecentImages4(context)
        imageList = imageList + newImages

        isLoading = false
    }
    // Request permissions
    LaunchedEffect(Unit) {
        permissionLauncher.launch(requiredPermissions)
        loadMoreImages(context)
    }
    // When permissions are granted, start CameraX functionality
    if (permissionsGranted) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Initialize camera preview using CameraX
        LaunchedEffect(cameraSelector) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraX", "Camera binding failed", e)
            }
        }

        // Bottom UI components: Flip camera, Capture button, Thumbnail bar
        Box(modifier = Modifier.fillMaxSize()) {
            // Camera flip button
            IconButton(
                onClick = {
                    cameraSelector =
                        if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        else
                            CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp, end = 30.dp)
                    .background(Color.White.copy(alpha = 0.8f), shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(R.drawable.flip_camera_android_24),
                    contentDescription = "Switch Camera",
                    modifier = Modifier.size(30.dp)
                )
            }

            Column(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (dragAmount < -20 && !isGalleryOpen) {
                                isGalleryOpen = true
                                launcher.launch("image/*")
                            }
                        }
                    }
                    .wrapContentSize()
                    .padding(bottom = 40.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider( modifier = Modifier.width(60.dp).padding(bottom = 10.dp),thickness=2.dp, color = Color.DarkGray)
                // LazyRow with infinite scroll
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(imageList) { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    navController.navigate("preview/$screen/${Uri.encode(uri.toString())}")
                                }
                        )
                    }

                    // Detect when the user reaches the end of the LazyRow to load more images
                    item {
                        LaunchedEffect(imageList.size) {
                            loadMoreImages(context)
                        }
                    }
                }
                // Capture button
                Box(
                    modifier = Modifier
                        .wrapContentSize()// Outer circular size
                        .padding(vertical = 16.dp)
                        .background(Color.White, shape = CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            val photoFile = File(
                                context.cacheDir,
                                "IMG_${System.currentTimeMillis()}.jpg"
                            )

                            val metadata = ImageCapture
                                .Metadata()
                                .apply {
                                    isReversedHorizontal =
                                        cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
                                }

                            val outputOptions = ImageCapture.OutputFileOptions
                                .Builder(photoFile)
                                .setMetadata(metadata)
                                .build()

                            imageCapture.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        Toast
                                            .makeText(
                                                context,
                                                "Saved: ${photoFile.name}",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                        selectedImageUri = Uri.fromFile(photoFile)
                                        navController.navigate(
                                            "preview/$screen/${
                                                Uri.encode(
                                                    selectedImageUri.toString()
                                                )
                                            }"
                                        )
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        Toast
                                            .makeText(
                                                context,
                                                "Failed: ${exception.message}",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "Capture",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(16.dp) // Camera icon size inside circle
                    )
                }

            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Waiting for permissions...", color = Color.White)
        }
    }
}


fun getRecentImages4(context: Context): List<Uri> {
    val imageUris = mutableListOf<Uri>()
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_ADDED
    )
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            imageUris.add(uri)
        }
    }
    return imageUris
}