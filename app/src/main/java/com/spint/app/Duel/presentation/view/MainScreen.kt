package com.spint.app.Duel.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
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




    if (viewModel.showPostCallDialog) {

        AlertDialog(
            onDismissRequest = {},
            title = { Text("What do you want to do?") },
            text = { Text("Do you want to send a friend request or report?") },

            confirmButton = {
                Button(onClick = {

                    // send friend request
                    // ApiClient.sendFriendRequest(otherUserId)

                    viewModel.showPostCallDialog = false
                    viewModel.callState = CallState.Matching

                    SocketManager.socket.emit(
                        "findMatch",
                        JSONObject().apply {
                            put("userId", UserObject.user.value.user)
                            put("name", UserObject.user.value.name)
                            put("profileImage", UserObject.user.value.profileImage)
                        }
                    )

                }) {
                    Text("Send Friend Request")
                }
            },

            dismissButton = {
                Button(onClick = {

                    viewModel.showPostCallDialog = false
                    viewModel.callState = CallState.Matching

                    SocketManager.socket.emit(
                        "findMatch",
                        JSONObject().apply {
                            put("userId", UserObject.user.value.user)
                            put("name", UserObject.user.value.name)
                            put("profileImage", UserObject.user.value.profileImage)
                        }
                    )

                }) {
                    Text("Skip")
                }
            }
        )
    }



    /* ------------------------------------------------------ */
    /* UI */
    /* ------------------------------------------------------ */

    Scaffold(
        topBar = {
//            if (callState is CallState.Idle) {
//                DuelTopBar { navController.navigateUp() }
//            }
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
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(width = 0.5.dp, color =Color(0xFFB7EAEA)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2A2A2A) //0xFF77B0B2
                        )
                    ) {
                        Text(
                            "Connect",
                            color = Color(0xFFB7EAEA),
                            fontFamily = Constants.FONT_LIGHT,
                            fontSize = 20.sp
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
                            LiveTimeTopRow()
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
                                Box(modifier=Modifier.align(Alignment.BottomCenter)) {
                                    CallFunctionalityButtons()
                                }
                            }
                        }
                    }
                }

                is CallState.InCall -> {

                    /* ------------------ REMOTE (TOP) ------------------ */
                    Column(modifier= Modifier.fillMaxSize()) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f)
                        ) {

                            AndroidView(
                                factory = { remoteView },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )

                            Row(
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .align(Alignment.BottomCenter)
                                    .clip(shape = RoundedCornerShape(5.dp))
                                    .background(color = Color.DarkGray),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 6.dp)
                                        .size(36.dp)
                                        .clip(shape = CircleShape)
                                ) {
                                    AsyncImage(
                                        model = imagePrefix + otherUserImage,
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Text(
                                    otherUserName,
                                    fontSize = 16.sp,
                                    fontFamily = Constants.FONT_LIGHT,
                                    color = Color.White
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(end = 6.dp)
                                        .wrapContentSize()
                                        .clip(shape = RoundedCornerShape(4.dp))
                                        .background(color = floatingActionBtnColor)
                                ) {
                                    Text(
                                        "+Friend",
                                        fontSize = 12.sp,
                                        fontFamily = Constants.FONT_LIGHT,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 20.dp, end = 16.dp)
                                    .align(Alignment.BottomEnd)
                                    .wrapContentSize()
                            ) {
                                Text(
                                    text = "${viewModel.remainingTime}s",
                                    color = Color.Red,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                )


                            }


                        }

                        /* ------------------ LOCAL (BOTTOM) ------------------ */

                        Box(
                            modifier = Modifier
                                // .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            AndroidView(
                                factory = { localView },
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .padding(bottom = 30.dp)
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(60.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.close),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable { viewModel.callState = CallState.Idle })
                                    Box(
                                        modifier = Modifier
                                            .height(32.dp)
                                            .wrapContentWidth()
                                            .clip(shape = RoundedCornerShape(4.dp))
                                            .background(color = floatingActionBtnColor)
                                    ) {
                                        Row(modifier= Modifier
                                            .padding(horizontal = 4.dp)
                                            .fillMaxHeight()
                                            .wrapContentWidth()
                                            .clickable {
                                                viewModel.endCall()

                                                SocketManager.socket.emit(
                                                    "skipMatch",
                                                    JSONObject().apply {
                                                        put("userId", UserObject.user.value.user)
                                                        put("name", UserObject.user.value.name)
                                                        put(
                                                            "profileImage",
                                                            UserObject.user.value.profileImage
                                                        )
                                                    }
                                                )

                                            }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(
                                                "Next",
                                                modifier = Modifier,
                                                fontSize = 18.sp,
                                                fontFamily = Constants.FONT_LIGHT,
                                                color = Color.White
                                            )
                                            Image(painter = painterResource(R.drawable.next),contentDescription = null,modifier= Modifier.size(22.dp), colorFilter = ColorFilter.tint(color=Color.White))
                                        }
                                    }

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
            SocketManager.disconnect()
        }
    }
}



@Composable
fun DuelUserCameraViewLocal(localView: SurfaceView) {

    val user = UserObject.user.collectAsState()
    val location = UserLocationObject.userLocation.collectAsState()
    val selectedColor=Color(0xFF58D7EA)

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .background(Color.DarkGray),
            contentAlignment = Alignment.TopCenter
        ) {
            AndroidView(
                factory = { localView },
                modifier = Modifier.fillMaxSize()
            )


//            Column(
//                modifier = Modifier
//                    .padding(bottom = 10.dp)
//                    .align(Alignment.BottomCenter),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    user.value.userName,
//                    fontFamily = Constants.ROBOTO_FLEX,
//                    fontSize = 24.sp,
//                    color = Color.White
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    "${location.value.city}, ${location.value.state}, ${location.value.country}",
//                    fontFamily = Constants.FONT_LIGHT,
//                    fontSize = 12.sp,
//                    color = Color.White
//                )
//            }
            //}
        }
        Spacer(modifier=Modifier.height(20.dp))
        var selectedVibe by remember { mutableStateOf("") }
        var selectedLookingFor by remember { mutableStateOf("") }

        Text("YOUR VIBE RIGHT NOW", color = Color.Gray, modifier = Modifier.padding(start = 10.dp), fontSize = 12.sp)

        val listOfVibe = listOf(Pair("music", R.drawable.spint1), Pair("deep talk", R.drawable.dating), Pair("just vibe", R.drawable.heart), Pair("spicy", R.drawable.alien))

        Row(
            modifier = Modifier
                .padding(start = 10.dp, top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            listOfVibe.forEach { item ->

                val isSelected = selectedVibe == item.first

                Card(
                    modifier = Modifier
                        .width(80.dp)
                        .height(60.dp)
                        .clickable {
                            selectedVibe = item.first
                        },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) selectedColor else Color.DarkGray
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor =Color(0xFF2A2A2A)
//                            if (isSelected) Color(0xFF77B0B2) //0xFF2A2A2A
//                            else Color(0xFF2A2A2A)
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(item.second),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            item.first,
                            fontSize = 14.sp,
                            color = if (isSelected) selectedColor else Color.LightGray,
                            fontFamily = Constants.FONT_MEDIUM
                        )
                    }
                }
            }
        }

        Text("LOOKING FOR", color = Color.Gray, modifier = Modifier.padding(start = 10.dp, top = 10.dp), fontSize = 12.sp)
        val listOf = listOf("make a friend", "just talk", "find plans")
        Row(
            modifier = Modifier
                .padding(start = 10.dp, top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf.forEach { item ->

                val isSelected = selectedLookingFor == item

                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            selectedLookingFor = item
                        },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) selectedColor else Color.DarkGray
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2A2A2A)
//                            if (isSelected) Color(0xFF77B0B2)
//                            else Color(0xFF2A2A2A)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            item,
                            fontSize = 14.sp,
                            color = if (isSelected) selectedColor else Color.LightGray,
                            fontFamily = Constants.FONT_LIGHT
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LiveTimeTopRow() {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 4.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "LIVE",
            modifier = Modifier.align(Alignment.TopStart),
            fontSize = 16.sp,
            color=Color.LightGray,
            fontFamily = Constants.FONT_MEDIUM
        )
        Text(
            "00:00",
            modifier = Modifier.align(Alignment.TopCenter),
            fontSize = 16.sp,
            color=Color.LightGray,
            fontFamily = Constants.FONT_MEDIUM
        )
        Box(modifier = Modifier
            .align(Alignment.TopEnd)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF2A2A2A)) ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.flip_camera_android_24),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.LightGray),
                    modifier = Modifier
                        .size(10.dp)
                )
                Text(
                    "report",
                    modifier = Modifier,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    fontFamily = Constants.FONT_MEDIUM
                )
            }
        }

    }

}
@Preview
@Composable
fun CallFunctionalityButtons() {
    Column(modifier= Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth()
        .wrapContentHeight()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFF2A2A2A))) {
                Image(
                    painter = painterResource(R.drawable.mic),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.LightGray),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp)
                )
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(50.dp)
                    .weight(1f),
                colors= ButtonDefaults.buttonColors(containerColor = Color(0xFF222728)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "+ Extend .0/2",
                    modifier = Modifier,
                    fontSize = 16.sp,
                    color=Color.LightGray,
                    fontFamily = Constants.FONT_MEDIUM
                )
            }
            Box(modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFF2A2A2A))) {
                Image(
                    painter = painterResource(R.drawable.cameranew),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.LightGray),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 10.dp, top = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // GO PUBLIC
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2A2A2A))
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.view),
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "Go Public",
                        fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                }
            }

            // NEXT
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2A2A2A))
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.next),
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "Next",
                        fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                }
            }

            // END
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF960B0B))
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.cross),
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "End",
                        fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun TimeUpDialog() {
    Dialog(
        onDismissRequest = {}
    ) {
        Column(modifier=Modifier.fillMaxWidth().wrapContentHeight(), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.clock_outlined),
                contentDescription = "",
                modifier = Modifier.size(40.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                "Time's up.",
                fontSize = 24.sp,
                fontFamily = Constants.FONT_MEDIUM,
                color = Color.White
            )
            Text(
                "Send them a note before they disappear",
                fontSize = 12.sp,
                fontFamily = Constants.FONT_LIGHT,
                color = Color.LightGray
            )
            Text(
                "Disappear if no reply in 10 mins.",
                fontSize = 12.sp,
                fontFamily = Constants.FONT_LIGHT,
                color = Color.Gray
            )
            OutlinedTextField(
                "",{},
                modifier=Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(80.dp),
                shape=RoundedCornerShape(12.dp),
                placeholder = {Text("say something real...", fontSize = 12.sp, color = Color.Gray)}
            )
            Button(onClick = {}, shape = RoundedCornerShape(12.dp),modifier=Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(R.drawable.chat_new),
                        contentDescription = "",
                        modifier = Modifier.padding(end=6.dp).size(14.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        "Send Note",
                        fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                }
            }
            Button(onClick = {}, shape = RoundedCornerShape(12.dp),modifier=Modifier.padding(horizontal = 16.dp).fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), border = BorderStroke(0.5.dp,color=Color(0xFF2A2A2A))) {
                    Text(
                        "Skip",
                        fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
            }
            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = {}, shape = RoundedCornerShape(12.dp),modifier=Modifier.height(60.dp).fillMaxWidth(0.5f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A2A)), border = BorderStroke(0.5.dp,color=Color(0xFF2A2A2A))) {
                    Column(modifier=Modifier, verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "how was it",
                            fontSize = 12.sp,
                            fontFamily = Constants.FONT_LIGHT,
                            color = Color.White
                        )
                        Row() {
                            Image(
                                painter = painterResource(R.drawable.person_new_filled),
                                contentDescription = "",
                                modifier = Modifier.padding(end = 6.dp).size(12.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                            Image(
                                painter = painterResource(R.drawable.person_new_filled),
                                contentDescription = "",
                                modifier = Modifier.padding(end = 6.dp).size(12.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                        }
                    }
                }
                Button(onClick = {}, shape = RoundedCornerShape(12.dp),modifier=Modifier.height(60.dp).fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A2A)), border = BorderStroke(0.5.dp,color=Color(0xFF2A2A2A))) {
                    Row(modifier=Modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(R.drawable.spint1),
                            contentDescription = "",
                            modifier = Modifier.padding(end = 6.dp).size(10.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(
                            "Duel again",
                            fontSize = 14.sp,
                            fontFamily = Constants.FONT_LIGHT,
                            color = Color.White
                        )
                    }
                }
            }

        }
    }
}