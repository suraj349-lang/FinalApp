package com.example.finalapp.screens.dialogBox

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.screens._1home.utils.OfferResponseDataAndAction
import com.example.finalapp.screens._4profile.createImageFile
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import com.example.finalapp.utils.RequestState
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.viewmodels.ImageUploadViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DropProfileDialog(authViewModel: AuthViewModel, eventsViewModel: EventsViewModel, imageUploadViewModel: ImageUploadViewModel, navController: NavHostController, onDismiss: () -> Unit) {
    var caption by remember{ mutableStateOf("testing") }
    val scope= rememberCoroutineScope()
    val context= LocalContext.current
    var enabled=true;
    val location by authViewModel.currentLocation.collectAsState()


    var uri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    var key by remember { mutableStateOf(false) }
    if (key) {
        ImageCaptureFromCameraForDropProfile{uri=it}
    }
    var keyForGallery by remember {
        mutableStateOf(0)
    }
    var activeBtnKey by remember {
        mutableStateOf(0)
    }
    var expirationTime by remember {
        mutableStateOf(
            when(activeBtnKey){
                0-> "12"
                1-> "24"
                2-> "168"
                else -> "infinite"
            }
        )
    }
    val dropProfileModel = DropProfileModel(
        image = "",
        location = authViewModel.address.value,
        message = caption,
        expirationTime = expirationTime,
        createdBy = "6680fef693d1e2645e19ee09"
    )

    var imageFile by mutableStateOf<File?>(null)
    if(keyForGallery!=0) {
        Log.d("Suraj", "DropProfileDialog:entered in gallery ")
        GalleryPickerForDropProfile(
            navController = navController,{imageFile=it}
        ) {
            Log.d("suraj", "DropProfileDialog: received image uri $it")
            uri = it
        }
    }
    val dropProfileState by imageUploadViewModel.dropProfileState.collectAsState()


    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(
        dismissOnBackPress = true,dismissOnClickOutside = false
    )
    ) {
        Card(
            shape = RoundedCornerShape((6.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    //.verticalScroll(enabled = true, state = rememberScrollState())
                    .background(Color.White),
                verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp), colors = CardDefaults.cardColors(containerColor = statusAndTopAppBarColor), shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp, topStart = 0.dp, topEnd = 0.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Image(painter = painterResource(id = R.drawable.app_icon), contentDescription ="app icon", modifier = Modifier
                            .size(40.dp)
                            .padding(start = 8.dp, end = 8.dp) , colorFilter = ColorFilter.tint(
                            topAppBarTextColor))
                        Text(text = "Drop Profile", modifier = Modifier
                            .fillMaxWidth()
                            , color = topAppBarTextColor, fontWeight = FontWeight.Normal,textAlign = TextAlign.Start,style = MaterialTheme.typography.titleMedium)
                    }


                }

                Card(
                    shape=RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .padding(8.dp),
                ) {
                    if(uri !=Uri.EMPTY){
                        GlideImage(
                            model = uri,
                            contentDescription = "",
                            transition = CrossFade,
                            contentScale = ContentScale.Crop
                        )
                    }else{
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxSize(0.5f)
                                    .padding(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .background(color = Color(0xFFFFFFFE))
                                            .padding(top = 16.dp)
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Choose from Camera / Gallery.",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )
                                    }

                                    Row(
                                        Modifier
                                            .fillMaxSize()
                                            .background(Color.White),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Box(modifier = Modifier.size(100.dp)) {
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Image(painterResource(id = R.drawable.camera),
                                                    contentDescription = "",
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clickable { key = !key }
                                                )
                                                Text(text = "Camera", modifier = Modifier)

                                            }
                                        }
                                        Box(modifier = Modifier.size(100.dp)) {
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Image(painterResource(id = R.drawable.gallery),
                                                    contentDescription = "",
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clickable {
                                                            keyForGallery = 1


                                                        })
                                                Text(text = "Gallery", modifier = Modifier)

                                            }
                                        }


                                    }
                                }
                            }
                        }

               }
                }
                Divider(modifier = Modifier
                    .fillMaxWidth(), thickness = 1.dp,color=Color(0xFFDCD6DD)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp, max = 100.dp)
                        .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                        .background(Color.White, RoundedCornerShape(5.dp)),
                    shape = RoundedCornerShape(5.dp),
                    value = caption,
                    onValueChange = { caption = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 4,
                    placeholder={ Text(text = "Enter caption")},
                )
                //Expiration time
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    , horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Availability Time :", color = Color.DarkGray, modifier = Modifier.fillMaxWidth() , fontSize = 12.sp, textAlign = TextAlign.Start)
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = { activeBtnKey=0 },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(activeBtnKey==0) statusAndTopAppBarColor else Color.LightGray ,
                                contentColor =if(activeBtnKey==0) topAppBarTextColor else Color.DarkGray
                        )
                        ) {
                            Text(text = "12 hrs")
                        }
                        Button(onClick = {activeBtnKey=1 },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(activeBtnKey==1) statusAndTopAppBarColor else Color.LightGray ,
                                contentColor =if(activeBtnKey==1) topAppBarTextColor else Color.DarkGray
                            )
                        ) {
                            Text(text = "24 hrs")
                        }
                        Button(onClick = { activeBtnKey=2 },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(activeBtnKey==2) statusAndTopAppBarColor else Color.LightGray ,
                                contentColor =if(activeBtnKey==2) topAppBarTextColor else Color.DarkGray
                            )
                        ) {
                            Text(text = "1 week")
                        }
                    }
                }
                Divider(modifier = Modifier
                    .fillMaxWidth(), thickness = 1.dp,color=Color(0xFFDCD6DD)
                )

                    Button(
                        onClick = {
                            eventsViewModel.key.value = 1
                            //todo later on turn enalbed to true
                          //  enabled = false;
                            imageFile?.let {
                                imageUploadViewModel.dropProfileModel.value=dropProfileModel
                                imageUploadViewModel.s3ImageUploadFunction("suraj3494",it)
                                }
                        },
                        shape= RoundedCornerShape(6.dp),
                        modifier= Modifier
                            .fillMaxWidth(1f)
                            .padding(15.dp),
                        enabled=enabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = statusAndTopAppBarColor,
                            contentColor = topAppBarTextColor,
                            disabledContainerColor= Color.LightGray,
                            disabledContentColor= Color.DarkGray
                        )
                    ) {
                        Text(text = "Drop Profile")
                    }

                    when (dropProfileState){
                        is RequestState.Loading ->{
                            DialogLoading()
                        }
                        is RequestState.Success->{
                            Toast.makeText(context,"Profile drop : SUCCESS", Toast.LENGTH_SHORT).show()
                            imageUploadViewModel.updateDropProfileStateToIdle()
                            onDismiss()
                        }
                        is RequestState.Error->{
                            Toast.makeText(context,"Error dropping profile",Toast.LENGTH_SHORT).show()
                        }
                        else->{}
                    }

                if(eventsViewModel.key.value==1){
                    Log.d("Data received","runned this")
                    OfferResponseDataAndAction(eventsViewModel,navController)

                }

            }
        }
    }
}

fun getImageRequestBody(context: Context, imageUri: Uri?): RequestBody? {
    imageUri ?: return null

    val filePath = getPathFromUri(context, imageUri) ?: return null
    val file = File(filePath)

    return RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
}

// Function to get file path from Uri (implementation depends on your app needs)
fun getPathFromUri(context: Context, uri: Uri): String? {
    // Implement logic to get file path from Uri (use ContentResolver, MediaStore, etc.)
    return uri.path // Replace with actual path retrieval logic
}

@Composable
fun ImageCaptureFromCameraForDropProfile(onUriChange:(Uri)->Unit) {

        val context = LocalContext.current
        val file = context.createImageFile()
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

        val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                capturedImageUri = uri
                onUriChange(uri)

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
}


