package com.example.finalapp.screens._3createEvent.privateCreateEvent

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants
import java.io.File


@Composable
fun CreateEventImageScreen(onImageUriChange:(Uri?)->Unit, onNextClicked:()->Unit) {
    val context = LocalContext.current

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
            result.uriContent?.let { onImageUriChange(it) }
        }
    }
    var showChooser by remember {
        mutableStateOf(false)
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { cropImageLauncher.launch(CropImageContractOptions(it, CropImageOptions())) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraImageUri.value != null) {
            cropImageLauncher.launch(CropImageContractOptions(cameraImageUri.value, CropImageOptions()))
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                File(context.cacheDir, "temp_image.jpg")
            )
            cameraImageUri.value = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    if (imageUri != null) {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
    }
    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (bitmap != null) {
                Image(
                    bitmap!!.asImageBitmap(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .fillMaxHeight(0.7f)
                        .border(width = 0.5.dp, color = Color.LightGray)
                )
                Text(text = "Edit image", fontWeight = FontWeight.Bold, fontFamily = Constants.DONGLE_BOLD, fontSize = 16.sp)
                Button(onClick =  onNextClicked ) {
                    Text(text = "Next")

                }
            } else {

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp, start = 20.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Add an Image", fontFamily = Constants.DONGLE_BOLD, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.camera_colored),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            "Take a photo",
                            fontSize = 24.sp,
                            fontFamily = Constants.DONGLE_BOLD,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                        == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            File(context.cacheDir, "temp_image.jpg")
                                        )
                                        cameraImageUri.value = uri
                                        cameraLauncher.launch(uri)
                                    } else {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                    showChooser = false
                                }

                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gallery_colored),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )

                        Text(
                            "Select from device",
                            fontSize = 24.sp,
                            fontFamily = Constants.DONGLE_BOLD,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    galleryLauncher.launch("image/*")
                                    showChooser = false
                                }

                        )
                    }
                }
            }
        }
    }
}
