package com.spint.app.screens._4profile


import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.spint.app.R
import java.io.File


@Composable
fun ImageCropperAndChooser(showChooser: Boolean,imageUriReceived:(Uri)->Unit) {
    val context = LocalContext.current

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
            result.uriContent?.let { imageUriReceived(it) }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { cropImageLauncher.launch(CropImageContractOptions(it, CropImageOptions())) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraImageUri.value != null) {
            cropImageLauncher.launch(CropImageContractOptions(cameraImageUri.value, CropImageOptions()))
        }
    }
   var showChooser2 by remember {
       mutableStateOf(true)
   }
    if (imageUri != null) {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
    }
  if(showChooser2) {
      Box(Modifier.fillMaxSize()) {
          Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Top,
              modifier = Modifier
                  .fillMaxSize()
                  .padding(top = 100.dp)
          ) {
              if (bitmap != null) {
                  Image(
                      bitmap!!.asImageBitmap(),
                      contentDescription = "",
                      contentScale = ContentScale.Crop,
                      modifier = Modifier
                          .clip(CircleShape)
                          .background(Color.Blue)
                          .size(150.dp)
                          .border(width = 1.dp, color = Color.Blue, shape = CircleShape)
                  )
              } else {
                  Image(
                      painter = painterResource(R.drawable.profile_new),
                      contentDescription = "",
                      modifier = Modifier
                          .clip(CircleShape)
                          .background(Color.Blue)
                          .size(150.dp)
                  )
              }

              Spacer(modifier = Modifier.height(32.dp))

              Icon(
                  painter = painterResource(R.drawable.cameranew),
                  contentDescription = "",
                  modifier = Modifier
                      .clip(CircleShape)
                      .background(Color.Gray)
                      .size(50.dp)
                      .clickable {
                          showChooser2 = false
                      }
                      .padding(10.dp),
                  tint = Color.White
              )
          }

          if (showChooser) {
              AlertDialog(
                  onDismissRequest = { showChooser2=false },
                  title = { Text("Select Image Source") },
                  text = {
                      Column {
                          Text(
                              "Camera",
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .clickable {
                                      val uri = FileProvider.getUriForFile(
                                          context,
                                          "${context.packageName}.provider",
                                          File(context.cacheDir, "temp_image.jpg")
                                      )
                                      cameraImageUri.value = uri
                                      cameraLauncher.launch(uri)
                                      showChooser2 = false
                                  }
                                  .padding(8.dp)
                          )
                          Text(
                              "Gallery",
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .clickable {
                                      galleryLauncher.launch("image/*")
                                      showChooser2 = false
                                  }
                                  .padding(8.dp)
                          )
                      }
                  },
                  confirmButton = {},
                  dismissButton = {}
              )
          }
      }
  }
}
