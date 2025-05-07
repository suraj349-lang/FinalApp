package com.example.finalapp.screens.common

import android.graphics.BitmapFactory
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.enums.ImageUploadScreens
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.viewmodels.ImageUploadViewModel


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

    // Load the original bitmap
    val originalBitmap = remember(uri) {
        val stream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(stream)
    }

    // Apply selected filter
    val filteredBitmap = remember(selectedFilter, originalBitmap) {
        originalBitmap?.let { applyFilter(it, selectedFilter) }
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            ImagePreviewTopBar{
                when(lastScreen){
                    ImageUploadScreens.PROFILE.screen->{
                        imageUploadViewModel.profileImageUri.value=uri
                        onDoneClicked()
                    }
                    ImageUploadScreens.CREATE_EVENT.screen->{
                        imageUploadViewModel.createEventImageUri.value=uri
                        onDoneClicked()
                    }
                    else->{
                        eventsViewModel.dropProfileUploadUri.value=uri
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
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
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
          Button(onClick = { onDoneClicked() }) {
              Text(text = "DONE", fontFamily = Constants.FONT_MEDIUM, fontSize = 20.sp)
          }
      }
  )
}