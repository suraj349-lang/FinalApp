package com.spint.app.screens.duel

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.spint.app.R
import com.spint.app.utils.UserLocation
import com.spint.app.utils.UserLocationObject
import com.spint.app.utils.UserObject
import com.spint.app.utils.constants.Constants
import kotlinx.coroutines.delay
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer


@Composable
fun DuelScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as Activity

    val cameraPermission = Manifest.permission.CAMERA
    val micPermission = Manifest.permission.RECORD_AUDIO

    var isConnected by remember { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(false) }

    val eglBase = remember { EglBase.create() }

    // Keep the views across recompositions
    val localView = remember { SurfaceViewRenderer(context) }
    val remoteView = remember { SurfaceViewRenderer(context) }

    // Initialize renderers safely
    DisposableEffect(localView) {
        localView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        localView.setEnableHardwareScaler(true)
        localView.init(eglBase.eglBaseContext, null)
        localView.setMirror(true)
        onDispose { localView.release() }
    }
    DisposableEffect(remoteView) {
        remoteView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        remoteView.setEnableHardwareScaler(true)
        remoteView.init(eglBase.eglBaseContext, null)
        remoteView.setMirror(false)
        onDispose { remoteView.release() }
    }

    val webRTCManager = remember { WebRTCManager(context, localView, remoteView, eglBase) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[cameraPermission] == true && permissions[micPermission] == true
        permissionsGranted = granted
        if (granted) {
            webRTCManager.initFactoryAndLocalTracks()
            webRTCManager.setupLocalVideo()
            webRTCManager.startLocalPreview()
        } else {
            Toast.makeText(context, "Camera and mic permissions are required.", Toast.LENGTH_LONG).show()
        }
    }

    // Ask for permissions on first load
    LaunchedEffect(Unit) {
        val hasCamera = ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
        val hasMic = ContextCompat.checkSelfPermission(context, micPermission) == PackageManager.PERMISSION_GRANTED
        if (hasCamera && hasMic) {
            permissionsGranted = true
            webRTCManager.initFactoryAndLocalTracks()
            webRTCManager.setupLocalVideo()
            webRTCManager.startLocalPreview()
        } else {
            permissionLauncher.launch(arrayOf(cameraPermission, micPermission))
        }
    }
    // Whether the UI is visible
    var currentScreen by remember{ mutableStateOf(SCREEN.THREE_FOUR) }
    var isVisible by remember { mutableStateOf(true) }

    // Used to trigger the 5s countdown when visible changes
    LaunchedEffect(key1=isVisible , key2 = isConnected) {
        if (isVisible) {
            // wait 5 seconds, then hide
            delay(5000)
            isVisible = false
        }
    }

    Scaffold(
        topBar = {
            if(!isConnected){
            DuelTopBar()
        }},
        bottomBar = {
            if (!isConnected){
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(Color.Black.copy(alpha = 0.9f))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (permissionsGranted) {
                                isConnected = !isConnected
                                if (isConnected) {
                                    webRTCManager.setupSocket()
                                } else {
                                    webRTCManager.release()
                                    isVisible = true         // show buttons again
                                    currentScreen = SCREEN.THREE_FOUR
                                }
                            } else {
                                permissionLauncher.launch(arrayOf(cameraPermission, micPermission))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1976D2)),
                        modifier = Modifier
                            .width(220.dp)
                            .height(45.dp),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(if (isConnected) "Disconnect" else "Connect", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 0.dp)
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .height(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
//                    Checkbox(
//                        checked = true,
//                        onCheckedChange = { /*TODO*/ },
//                        colors = CheckboxDefaults.colors(
//                            checkedColor = Color(0xFF1976D2),
//                            checkmarkColor = Color.White
//                        ),
//                        modifier = Modifier.size(10.dp)
//                    )
                        Text(
                            text = "By clicking on connect you agree to our app policy on duel feature and avoid nudity, threat, hate and crime. Click here to know ${Constants.APP_NAME.lowercase()}'s DUEL POLICY.",
                            maxLines = 2,
                            overflow = TextOverflow.Visible,
                            fontFamily = Constants.FONT_LIGHT,
                            fontSize = 6.5.sp,color= Color.LightGray
                        )
                    }

//                Button(
//                    onClick = { /* TODO: Next random user */ },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1976D2)),
//                    modifier = Modifier
//                        .width(100.dp)
//                        .height(45.dp),
//                    shape = RoundedCornerShape(50)
//                ) {
//                    Text("Next >", color = Color.White, fontFamily = Constants.FONT_LIGHT)
//                }
                }
            }
        },
        floatingActionButton = {
            if (isConnected) {
                DuelOptions(
                    isVisible,
                    onCloseClicked = {
                        isConnected=false
                        webRTCManager.release();

                        navController.navigateUp()
                    }) {
                    isVisible = true
                }
            }},
        floatingActionButtonPosition = androidx.compose.material.FabPosition.Center
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // .padding(top = 1.dp)
                .padding(padding)
                .background(Constants.HOME_TOP_BAR_COLOR),
            contentAlignment = Alignment.Center
        ) {
            BackHandler(true) {
                webRTCManager.release()
                navController.navigateUp()
            }
            when {
                !permissionsGranted -> {
                    Text("Please grant camera and mic permissions to continue.", color = Color.White)
                }

                isConnected -> {

                    // Remote user view screen
                    AndroidView(factory = {
                        (remoteView.parent as? ViewGroup)?.removeView(remoteView)
                        remoteView
                    }, modifier = Modifier.align(Alignment.TopCenter).fillMaxHeight(0.5f).fillMaxWidth()
                    )

                    Box(modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .wrapContentSize()){
                        ScreenOptions(){
                            currentScreen=it
                        }
                    }
                    


                    // current user screen

                    Box(
                        modifier = Modifier
                            .clickable { isVisible = true }
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        AndroidView( factory = { context ->
                            (localView.parent as? ViewGroup)?.let { parent ->
                                parent.clipToPadding = true
                                parent.clipChildren = true
                            }
                            (localView.parent as? ViewGroup)?.removeView(localView)
                            localView
                        } , modifier = Modifier.fillMaxSize())
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = LinearEasing)),
                            exit = fadeOut(
                                animationSpec = tween(
                                    durationMillis = 700, // fade-out duration
                                    easing = LinearEasing
                                )
                            )
                        ) {
                        Column(modifier = Modifier
                            .align(Alignment.TopStart)
                            .wrapContentSize()
                            .padding(16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.dating),
                                    contentDescription = "",
                                    modifier = Modifier.size(34.dp)
                                )
                                Text(
                                    text = "Mood",
                                    fontFamily = Constants.FONT_LIGHT,
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                else -> {
                    DuelFirstScreenLocal(localView)
                }
            }
        }
    }
}

@Composable
fun DuelFirstScreenLocal(localView: SurfaceViewRenderer) {
    val user= UserObject.user.collectAsState()
    val location= UserLocationObject.userLocation.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                // .size(240.dp, 320.dp)
                .background(Color.DarkGray),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                AndroidView(
                    factory = {
                        (localView.parent as? ViewGroup)?.removeView(localView)
                        localView
                    },
                    modifier = Modifier.padding(top = 10.dp).size(200.dp).clip(shape = CircleShape)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(user.value.userName, fontFamily = Constants.ROBOTO_FLEX, fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(location.value.city.toString()+", " + location.value.state.toString() +", " +location.value.country.toString(), fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp, color = Color.White)

            }


            // 🟢 2. Overlay icons on top-right
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    // .fillMaxHeight()
                    .width(60.dp)
                    .padding(8.dp), // optional
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DuelCameraEdit(R.drawable.edit_new)
                DuelCameraEdit(R.drawable.camera)
                DuelCameraEdit(R.drawable.filter)
                DuelCameraEdit(R.drawable.menu,false,20)

            }
        }
        Box(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Looking For:", fontFamily = Constants.FONT_LIGHT, fontSize = 14.sp,color= Color.White)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    val listOfTypes= listOf<String>("Male","Female","Others","Random")
                    listOfTypes.forEach {
                        Card(Modifier.wrapContentSize(), backgroundColor = Color.DarkGray, contentColor = Color.White) {
                            Text(text = it, fontFamily = Constants.FONT_LIGHT, fontSize = 18.sp,color= Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                InterestSelector()

                MoodSelector()

            }

        }

    }
}

@Composable
fun InterestSelector(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val list = listOf("Happy", "Sad", "Bored", "Excited")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Matching on:",
            fontFamily = Constants.FONT_LIGHT,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp), modifier = Modifier.clickable { expanded = true }.clip(shape = RoundedCornerShape(12.dp)).background(Color.Gray).wrapContentWidth().height(40.dp)) {
                Text(
                    text = "Mood",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
                Image(painter=painterResource(R.drawable.arrow_down), contentDescription = "", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(color = Color.White))
            }


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(100.dp)
            ) {
                list.forEach { item ->
                    DropdownMenuItem(onClick = {
                        // handle selection
                        println("Selected: $item")
                        expanded = false
                    }) {
                        Text(text = item)
                    }
                }
            }
        }
    }

}
@Composable
fun ScreenOptions(onScreenClicked: (screen:SCREEN) -> Unit) {
    val listOfScreenOrientation= listOf<ScreenOrientation>(
        ScreenOrientation(R.drawable.baseline_crop_24,SCREEN.HALF),
//        ScreenOrientation(R.drawable.baseline_crop_24,SCREEN.THREE_FOUR),
//        ScreenOrientation(R.drawable.baseline_crop_24,SCREEN.FULL),
        ScreenOrientation(R.drawable.baseline_crop_24,SCREEN.FLOAT),
    )
    Column(modifier = Modifier
        .wrapContentHeight()
        .width(60.dp), verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        listOfScreenOrientation.forEach {
            Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = it.image), contentDescription = "",
                    Modifier
                        .size(30.dp)
                        .clickable { onScreenClicked(it.screen) }, colorFilter = ColorFilter.tint(Color.LightGray))
                Text(text = it.screen.screen_name, fontFamily = Constants.FONT_MEDIUM, fontSize =10.sp,color= Color.LightGray.copy(alpha = 0.9f))
            }
        }
    }
}
enum class SCREEN(val screen_name:String,val flo:Double) {
    HALF("1 : 1",0.5),
    THREE_FOUR("3 : 4",0.35),
    FULL("full",0.0),
    FLOAT("float",0.6)
}
data class DualScreenOptions(
    val id:Int,
    val name:String
)
@Composable
fun DuelOptions(isVisible:Boolean,onCloseClicked:()->Unit,onScreenClicked:()->Unit) {
    val listOfImages= listOf<DualScreenOptions>(DualScreenOptions(R.drawable.cross,"end"),
        DualScreenOptions(R.drawable.add,"+friend"),
        DualScreenOptions(R.drawable.flip_camera_android_24,"flip"),
        DualScreenOptions(R.drawable.add_photo_by_camera,"screenshot"),
        DualScreenOptions(R.drawable.next,"next")
    )
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 500, // fade-in duration
                easing = LinearEasing
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 700, // fade-out duration
                easing = LinearEasing
            )
        )
    ) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        listOfImages.forEach {
     //   Card(modifier = Modifier.wrapContentSize(), shape = CircleShape,backgroundColor= Color.Transparent) {
            Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id =it.id), contentDescription = "", modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable {
                        if (it.name == "end") {
                            onCloseClicked()
                        }
                    }
                    .size(36.dp), colorFilter = ColorFilter.tint(Color.White))
                Text(text = it.name, fontFamily = Constants.FONT_LIGHT, fontSize = 9.sp,color= Color.White)
            }
    //   }

        }
    }
}}

data class ScreenOrientation(
    val image:Int,
    val screen: SCREEN,
)

@Composable
fun DuelCameraEdit(id:Int,tint:Boolean=true,size:Int=28) {

    Image(
        painter = painterResource(id = id),
        contentDescription = "",
        modifier = Modifier.size(size.dp),
        //colorFilter = if (tint) ColorFilter.tint(Color.White) else ColorFilter.tint(Color.Transparent)
    )
}


@Composable
fun MoodSelector() {
    var selectedMood by remember { mutableStateOf<String?>(moods[0].first) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            ,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(moods) { emoji ->
            val isSelected = emoji.first == selectedMood
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .clickable { selectedMood = emoji.first },
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = emoji.first,
                        fontSize = 20.sp,
                        color = if (isSelected) Color.Black else Color.White
                    )
                    Text(
                        text = emoji.second,
                        fontSize = 10.sp,
                        color = if (isSelected) Color.Black else Color.White
                    )
                }

            }
        }
    }
}


 val  moods = listOf<Pair<String, String>>(
     Pair("😊", "happy"),
     Pair("😎", "cool"),
     Pair("😢", "sad"),
     Pair("😡", "angry"),
     Pair("🤔", "thinking"),
     Pair("😍", "in love"),
     Pair("🥰", "affectionate"),
     Pair("😂", "laughing")
)