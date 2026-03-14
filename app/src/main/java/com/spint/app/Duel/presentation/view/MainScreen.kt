package com.spint.app.Duel.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceView
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.spint.app.Duel.data.ApiClient
import com.spint.app.Duel.data.CallState
import com.spint.app.Duel.data.MatchRequest
import com.spint.app.Duel.data.SocketManager
import com.spint.app.Duel.presentation.viewModel.CallViewModel
import com.spint.app.R
import com.spint.app.screens.duel.DuelTopBar
import com.spint.app.screens.duel.FlippingIconLoader
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.UserLocationObject
import com.spint.app.utils.UserObject
import com.spint.app.utils.constants.Constants
import io.agora.rtc2.RtcEngine
import kotlinx.coroutines.launch
import org.json.JSONObject


@Composable
fun DuelScreen2(
    navController: NavHostController,
    viewModel: CallViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val cameraPermission = Manifest.permission.CAMERA
    val micPermission = Manifest.permission.RECORD_AUDIO

    var permissionsGranted by remember { mutableStateOf(false) }
    val callState = viewModel.callState

    // Agora renderer views (IMPORTANT: remember them)
    val localView = remember { RtcEngine.CreateRendererView(context) }
    val remoteView = remember { RtcEngine.CreateRendererView(context) }
    val remoteUid = viewModel.remoteUid

    LaunchedEffect(Unit) {
        localView.setZOrderMediaOverlay(true)
    }



    /* ------------------------------------------------------ */
    /* PERMISSIONS */
    /* ------------------------------------------------------ */

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted =
            permissions[cameraPermission] == true &&
                    permissions[micPermission] == true

        permissionsGranted = granted

        if (granted) {
            viewModel.initAgora(context, "68f32fbde6824f98bebd21083c175e3e")
            viewModel.setupLocalVideo(localView)
            viewModel.startPreview()

        } else {
            Toast.makeText(
                context,
                "Camera & mic permission required",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    LaunchedEffect(Unit) {
        SocketManager.connect()
    }
    LaunchedEffect(remoteUid) {
        remoteUid?.let { uid ->
            viewModel.setupRemoteVideo(remoteView, uid)
        }
    }



    LaunchedEffect(Unit) {
        val hasCamera = ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
        val hasMic = ContextCompat.checkSelfPermission(context, micPermission) == PackageManager.PERMISSION_GRANTED

        if (hasCamera && hasMic) {
            permissionsGranted = true
            viewModel.initAgora(context, "68f32fbde6824f98bebd21083c175e3e")
            viewModel.setupLocalVideo(localView)
            viewModel.startPreview()   // ← ADD THIS
        } else {
            permissionLauncher.launch(arrayOf(cameraPermission, micPermission))
        }
    }
    var otherUserId by remember { mutableStateOf("") }
    var otherUserName by remember { mutableStateOf("") }
    var otherUserImage by remember { mutableStateOf("") }
    DisposableEffect(Unit) {

        val listener = { args: Array<Any> ->

            val data = args[0] as JSONObject

            val channel = data.getString("channelName")
            val token = data.getString("token")
            val expires = data.getInt("expiresIn")
            val otherUser = data.getJSONObject("otherUser")

            otherUserId = otherUser.getString("userId")
            otherUserName = otherUser.getString("name")
            otherUserImage = otherUser.getString("profileImage")

            viewModel.joinCall(
                token = token,
                channel = channel,
                uid = 0,//UserObject.user.value.user.toInt(),
                expiresIn = expires
            )
        }

        SocketManager.socket.on("matchFound", listener)

        onDispose {
            SocketManager.socket.off("matchFound", listener)
        }
    }


    /* ------------------------------------------------------ */
    /* UI */
    /* ------------------------------------------------------ */

    Scaffold(
        topBar = {
            if (callState is CallState.Idle) {
                DuelTopBar { navController.navigateUp() }
            }
        },
        bottomBar = {
            if (callState is CallState.Idle ) {

                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(Color.Black)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(
                        onClick = {
                            if (!permissionsGranted) {
                                permissionLauncher.launch(
                                    arrayOf(cameraPermission, micPermission)
                                )
                                return@Button
                            }
                            viewModel.callState = CallState.Matching

                            SocketManager.socket.emit(
                                "findMatch",
                                JSONObject().apply {
                                    put("userId", UserObject.user.value.user)
                                    put("name", UserObject.user.value.name)
                                    put("profileImage", UserObject.user.value.profileImage)
                                }
                            )


                        },
                        modifier = Modifier
                            .width(220.dp)
                            .height(45.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2)
                        )
                    ) {
                        Text(
                            "Connect",
                            color = Color.White,
                            fontFamily = Constants.FONT_MEDIUM
                        )
                    }
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {

            when (callState) {

                is CallState.Idle -> {
                    DuelUserCameraViewLocal(localView)
                }
                is CallState.Searching -> {

                }

                is CallState.Matching -> {
                    if(remoteUid == null){
                        Box(modifier=Modifier.fillMaxSize()) {
                            FlippingIconLoader(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.5f),
                                icon = R.drawable.spint1
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.5f)
                            ) {
                                AndroidView(
                                    factory = { localView },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }

                is CallState.InCall -> {

                    /* ------------------ REMOTE (TOP) ------------------ */

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                    ) {

                        AndroidView(
                            factory = { remoteView },
                            modifier = Modifier.fillMaxSize()
                        )
                        Row(modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.TopStart), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(modifier=Modifier
                                .size(36.dp)
                                .clip(shape = CircleShape)) {
                                AsyncImage(
                                    model = imagePrefix + otherUserImage,
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Text(otherUserName, fontSize = 16.sp, fontFamily = Constants.USER_NAME_FONT, color = Color.White)
                            Box(modifier=Modifier
                                .wrapContentSize()
                                .clip(shape = RoundedCornerShape(4.dp))
                                .background(color = floatingActionBtnColor)) {
                                Text("+Friend", fontSize = 12.sp, fontFamily = Constants.FONT_LIGHT, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp))
                            }
                        }


                    }

                    /* ------------------ LOCAL (BOTTOM) ------------------ */

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                    ) {
                        AndroidView(
                            factory = { localView },
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(modifier = Modifier.padding(bottom = 30.dp).align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(60.dp)) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.close),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable { viewModel.endCall() })
                                Box(modifier=Modifier.wrapContentSize().padding(horizontal = 4.dp).clip(shape = RoundedCornerShape(4.dp)).background(color=floatingActionBtnColor)) {
                                    Text(
                                        "Next",
                                        modifier = Modifier,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    /* ------------------------------------------------------ */
    /* CLEANUP */
    /* ------------------------------------------------------ */

    DisposableEffect(Unit) {
        onDispose {
            viewModel.endCall()
        }
    }
}

@Composable
fun DuelUserCameraViewLocal(localView: SurfaceView) {

    val user = UserObject.user.collectAsState()
    val location = UserLocationObject.userLocation.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(Color.DarkGray),
            contentAlignment = Alignment.TopCenter
        ) {
            AndroidView(
                factory = { localView },
                modifier = Modifier.fillMaxSize()
            )


            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    user.value.userName,
                    fontFamily = Constants.ROBOTO_FLEX,
                    fontSize = 24.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "${location.value.city}, ${location.value.state}, ${location.value.country}",
                    fontFamily = Constants.FONT_LIGHT,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            //}
        }
        Spacer(modifier=Modifier.height(20.dp))
        Text("Interested in?", color = Color.White)
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("MALE", color = Color.White)
            Text("FEMALE", color = Color.White)
            Text("OTHERS", color = Color.White)
        }
        Text("Proximity", color = Color.White)
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Nearby", color = Color.White)
            Text("World", color = Color.White)
            Text("Interest", color = Color.White)
        }
        Text("Timer", color = Color.White)
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("5 sec", color = Color.White)
            Text("30 sec", color = Color.White)
            Text("60 sec", color = Color.White)
        }
    }
}
