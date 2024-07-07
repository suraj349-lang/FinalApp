package com.example.finalapp.screens.dialogBox

import android.Manifest
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.offer.OfferViewModel
import com.example.finalapp.screens.OfferResponseDataAndAction
import com.example.finalapp.screens.profile.createImageFile
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import com.example.finalapp.utils.RequestState
import kotlinx.coroutines.launch


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DropProfileDialog(authViewModel:AuthViewModel,offerViewModel: OfferViewModel,navController: NavHostController, onDismiss: () -> Unit) {
    var offerTextField:String by remember{ mutableStateOf("") }
    val context= LocalContext.current
    var messageText:String by remember{ mutableStateOf("") }
    var placesText:String by remember{ mutableStateOf("") }
    val scope= rememberCoroutineScope()
    var enabled=true;
    val address by authViewModel.address.collectAsState()
    var uri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    var key by remember { mutableStateOf(false) }
    if (key) {
        ImageCaptureFromCameraForDropProfile{uri=it}
    }


    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(
        dismissOnBackPress = true,dismissOnClickOutside = true
    )
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape=RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(8.dp)
                ) {
                    if(uri !=Uri.EMPTY){
                        GlideImage(
                            model = uri,
                            contentDescription = "",
                            transition = CrossFade
                            , contentScale = ContentScale.Crop
                        )
                    }else{
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier
                                        .background(color = Color(0xFFFFFFFE))
                                        .padding(top = 16.dp)
                                        .fillMaxWidth()
                                        .wrapContentHeight(), horizontalArrangement = Arrangement.Center
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
                                                        navController.navigate(SCREENS.GALLERY.route)
                                                    })
                                            Text(text = "Gallery", modifier = Modifier)

                                        }
                                    }


                                }
                            }
                        }


               }
                }
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp), thickness = 2.dp,color=Color(
                    0xFF560464
                )
                )
                Text(text = "Location:", color = Color(0xFF661FE6), modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp), textAlign = TextAlign.Start)
                //Location Text
                Text(address, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp), color = Color.Blue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                //Google places to search to show nearby places
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, top = 8.dp, end = 15.dp)
                        .background(Color.White, RoundedCornerShape(5.dp)),
                    shape = RoundedCornerShape(5.dp),
                    value = placesText,
                    onValueChange = { placesText = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    placeholder={ Text(text = "Search a Landmark")},
                    label = { Text(text = "Search a Landmark")}
                )
                //message
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                        .background(Color.White, RoundedCornerShape(5.dp)),
                    shape = RoundedCornerShape(5.dp),
                    value = messageText,
                    onValueChange = { messageText = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 1,
                    placeholder={ Text(text = "Message")},
                    label = { Text(text = "Message")},
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFEBD515), unfocusedContainerColor = Color(0xFFEBD515))
                )
                //Expiration time
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    .background(Color(0xFFEAE7F0)), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Expiration Time", color = Color.DarkGray, modifier = Modifier.padding(8.dp))
                    Text(text = "08:00 hrs", color = Color.Black, modifier = Modifier.padding(8.dp))
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                    OutlinedButton(onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth(0.5f)
                            .padding(8.dp)) {
                        Text(text = "Cancel", color = Color.Red)
                    }
                    Button(onClick = {
                            offerViewModel.key.value = 1;
                            enabled=false;
                            scope.launch {
                                offerViewModel.dropProfile(DropProfileModel(image = uri.toString(), location = authViewModel.address.value, landmark = "",message="", expirationTime = "")); }
                           },
                        Modifier
                            .fillMaxWidth(1f)
                            .padding(8.dp),
                        enabled=enabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = statusAndTopAppBarColor,
                            contentColor = topAppBarTextColor,
                            disabledContainerColor= statusAndTopAppBarColor,
                            disabledContentColor= topAppBarTextColor
                        )
                    ) {
                        Text(text = "Upload")
                    }

                    when (val result=offerViewModel.dropProfileResponse.value){
                        is RequestState.Success->{

                            Toast.makeText(context,"${result.data}", Toast.LENGTH_SHORT).show()
                           onDismiss()

                        }
                        is RequestState.Error->{
                            Log.d("Data received",result.error.message.toString())
                            Toast.makeText(context,"$result", Toast.LENGTH_SHORT).show()
                        }
                        RequestState.Loading->{
                            CircularProgressIndicator(color = Color(0xFF1289BE))
                        }
                        RequestState.Idle->{
                            CircularProgressIndicator(color = Color(0xFF1289BE))

                        }

                    }
                }
                if(offerViewModel.key.value==1){
                    Log.d("Data received","runned this")
                    OfferResponseDataAndAction(offerViewModel,navController)

                }

            }
        }
    }
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


