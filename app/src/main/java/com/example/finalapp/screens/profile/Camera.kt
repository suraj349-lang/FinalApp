package com.example.finalapp.screens.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract.Profile
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.finalapp.R
import com.example.finalapp.network.ApiService
import com.example.finalapp.offer.OfferRepository
import com.example.finalapp.utils.Constants.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository):ViewModel(){

    val imageUri= mutableStateOf<Uri>(Uri.EMPTY)
    suspend fun uploadImage(context: Context,uri: Uri):String{
       return repository.uploadImage(context,uri)
    }

}
@Composable
fun ImageCaptureFromCamera () {
    val context = LocalContext.current
    val file = context.createImageFile();
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )
    val viewmodel= hiltViewModel<ProfileViewModel>()
    var imageUploadResponse by remember{mutableStateOf("")}


    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
            viewmodel.imageUri.value=uri
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
        LaunchedEffect(key1 = true) {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)

            }
        }
      //  if (capturedImageUri.path?.isNotEmpty() == true) {

          if (capturedImageUri!= Uri.EMPTY) {
            val scope= rememberCoroutineScope()
            Image(
                modifier = Modifier.padding(16.dp, 8.dp),
                painter = rememberImagePainter(viewmodel.imageUri.value),
                //painter = painterResource(id = R.drawable.profile_image_1),
                contentDescription = ""
            )
            Log.d(TAG, "Image captured uri is: ${capturedImageUri}")
            LaunchedEffect(key1 = true ) {
                scope.launch {
                    Log.d(TAG, "ImageCaptureFromCamera: started")
                    try {
                        imageUploadResponse =
                            viewmodel.uploadImage(context, viewmodel.imageUri.value)
                        Log.d(TAG, "ImageCaptureFromCamera:${imageUploadResponse} ")
                    }catch (e:Exception){
                        Log.d(TAG, e.message.toString())
                    }
                }
            }

//            Log.d(TAG, capturedImageUri.toString())
            // saveImageToGallery(context,capturedImageUri)
        }else{
            Surface(Modifier.fillMaxSize()) {
               Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="" )

            }
        }

    }
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    //val timeStamp= SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName="JPEG_"
    val image= File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}