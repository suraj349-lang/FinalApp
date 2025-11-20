package com.spint.app.screens.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.R
import com.spint.app.enums.ImageUploadScreens
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.EventsViewModel
import com.spint.app.viewmodels.ImageUploadViewModel
import java.io.File


@Composable
fun ImagePreviewScreen(
    uri: Uri,
    lastScreen: String,
    imageUploadViewModel: ImageUploadViewModel,
    eventsViewModel: EventsViewModel,
    onDoneClicked: () -> Unit,
    onClose: () -> Unit
) {
    BackHandler(onBack = onClose)
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf(FilterType.Original) }

    val originalBitmap = remember(uri) {
        loadBitmapSmart(context, uri)
    }


    // Apply selected filter
    val filteredBitmap = remember(selectedFilter, originalBitmap) {
        originalBitmap?.let { applyFilter(it, selectedFilter) }
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            ImagePreviewTopBar{
                val finalUri =
                    if (filteredBitmap != null)
                        saveBitmapToFile(context, filteredBitmap)
                    else
                        uri // fallback
                when(lastScreen){
                    ImageUploadScreens.PROFILE.screen->{
                        imageUploadViewModel.profileImageUri.value=finalUri
                        onDoneClicked()
                    }
                    ImageUploadScreens.CREATE_EVENT.screen->{
                        imageUploadViewModel.createEventImageUri.value=finalUri
                        onDoneClicked()
                    }
                    else->{
                        eventsViewModel.dropProfileUploadUri.value=finalUri
                        onDoneClicked()
                    }
                }
            }
        }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Image preview
                filteredBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                // Filter selector
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(FilterType.values()) { filter ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { selectedFilter = filter }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        if (filter == selectedFilter) Color.DarkGray else Color.Gray,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = filter.name.first().toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Text(
                                text = filter.label,
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Close button
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {

                Icon(
                    painter = painterResource(R.drawable.cross),
                    contentDescription = ""
                    ,tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePreviewTopBar(onDoneClicked:()->Unit) {
  TopAppBar(
      title = { },
      actions = {
          Button(onClick = { onDoneClicked() }, colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor)) {
              Text(text = "Done", fontFamily = Constants.FONT_LIGHT, fontSize = 20.sp)
          }
      },
      colors =TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Black)
  )
}

    fun loadBitmapSmart(context: Context, uri: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val bitmap = BitmapFactory.decodeStream(input)

        // Detect CameraX output images (your naming patterns)
        val isCameraXImage =
            uri.path?.contains("FINAL_IMG_") == true ||
                    uri.path?.contains("IMG_") == true

        // CameraX images → DO NOT rotate (CameraX already outputs correct orientation)
        if (isCameraXImage) {
            return bitmap
        }

        // Gallery images → apply EXIF rotation normally
        val exif = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        val rotationDegrees = when (
            exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        ) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        val matrix = Matrix()
        if (rotationDegrees != 0f) {
            matrix.postRotate(rotationDegrees)
        }

        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
    }
fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "FILTERED_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return Uri.fromFile(file)
}
