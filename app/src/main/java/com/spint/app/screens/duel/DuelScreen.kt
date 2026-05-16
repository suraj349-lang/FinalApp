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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.spint.app.R
import com.spint.app.navigation.SCREENS
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.UserLocationObject
import com.spint.app.utils.UserObject
import com.spint.app.utils.constants.Constants
import kotlinx.coroutines.delay
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

/*
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
    val webRTCManager = remember { WebRTCManager(context, localView, remoteView, eglBase) }
    val  isRemoteUserConnected by webRTCManager.isRemoteUserConnected.collectAsState()

    BackHandler(true) {
        navController.navigate(SCREENS.HOME.route)
      webRTCManager.release()

    }




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
            if (!isConnected) {
                DuelTopBar(){navController.navigateUp()}
            }
        },
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

                        isConnected=false
                    },
                    onNextClicked = {webRTCManager.nextUser()}) {
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
               // webRTCManager.release()
                navController.navigateUp()
                isConnected=false
            }
            when {
                !permissionsGranted -> {
                    Text("Please grant camera and mic permissions to continue.", color = Color.White)
                }

                isConnected -> {

                    // Remote user view screen
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()) {
                        if (!isRemoteUserConnected) {

                            // ⏳ SHOW LOADING UNTIL THE REMOTE USER JOINS
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.5f)) {
                                FlippingIconLoader(modifier = Modifier.align(Alignment.Center), icon = R.drawable.spint1)
                            }

                        } else {
                            AndroidView(
                                factory = {
                                    (remoteView.parent as? ViewGroup)?.removeView(remoteView)
                                    remoteView
                                }, modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.5f)
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .wrapContentSize()
                            ) {
                                ScreenOptions() {
                                    currentScreen = it
                                }
                            }
                        }
                    }


                    //--------------------------------------------------------------------------------------------------
                    // --current user screen-----------------------------------------------------------------------------

                    Box(
                        modifier = Modifier
                            .clickable { isVisible = true }
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                    ) {
                        AndroidView( factory = { context ->
                            (localView.parent as? ViewGroup)?.let { parent ->
                                parent.clipToPadding = true
                                parent.clipChildren = true
                            }
                            (localView.parent as? ViewGroup)?.removeView(localView)
                            localView
                        } , modifier = Modifier.fillMaxSize())
//                        AnimatedVisibility(
//                            visible = isVisible,
//                            enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = LinearEasing)),
//                            exit = fadeOut(
//                                animationSpec = tween(
//                                    durationMillis = 700, // fade-out duration
//                                    easing = LinearEasing
//                                )
//                            )
//                        ) {
//                        Column(modifier = Modifier
//                            .align(Alignment.TopStart)
//                            .wrapContentSize()
//                            .padding(16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.dating),
//                                    contentDescription = "",
//                                    modifier = Modifier.size(34.dp)
//                                )
//                                Text(
//                                    text = "Mood",
//                                    fontFamily = Constants.FONT_LIGHT,
//                                    fontSize = 10.sp,
//                                    color = Color.White
//                                )
//                            }
                       // }
                    }
                }

                else -> {
                    DuelFirstScreenLocal(localView)
                }
            }
        }
    }
}
*/
@Composable
fun DuelFirstScreenLocal(localView: SurfaceViewRenderer) {
    val user= UserObject.user.collectAsState()
    val location= UserLocationObject.userLocation.collectAsState()
    var isBlur by remember { mutableStateOf(false) }
    var isPrivate by remember { mutableStateOf(false) }
    val categories = listOf(
        MatchCategory.MoodCategory(moodList),
        MatchCategory.Sports,
        MatchCategory.Entertainment,
        MatchCategory.Lifestyle,
        MatchCategory.Technology,
        MatchCategory.Relationships,
        MatchCategory.RandomFun
    )
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
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(200.dp)
                        .clip(shape = CircleShape)
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
                PrivatePublicSwitch(isSelected =isPrivate ,{isPrivate=!isPrivate})
                DuelCameraEdit(title = "blur",R.drawable.blur_outlined, unSelectedIcon = R.drawable.blur_outlined, isSelected = isBlur, tint = isBlur, onClick = {isBlur= ! isBlur})
                DuelCameraEdit("filter",R.drawable.filter2, tint = false)
                DuelCameraEdit("menu",R.drawable.menu,tint=false,size=20)

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
                GenderSelection()

                Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = Color.Gray)
                MatchScreen(categories)

            }

        }

    }
}
@Composable
fun SubtopicSelector(
    subtopics: List<MatchSubtopic>,
    selected: MatchSubtopic?,
    onSelect: (MatchSubtopic) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        subtopics.forEach { topic ->
            val isSelected = topic == selected
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) Color.White else Color.DarkGray)
                    .clickable { onSelect(topic) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.name,
                    color = if (isSelected) Color.Black else Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
@Composable
fun MatchScreen(categories: List<MatchCategory>) {

    var selectedCategory by remember { mutableStateOf<MatchCategory>(MatchCategory.MoodCategory(moodList)) }
    var selectedMood by remember { mutableStateOf<MoodItem?>(null) }
    var selectedSubtopic by remember { mutableStateOf<MatchSubtopic?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        InterestSelector(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = {
                selectedCategory = it
                selectedMood = null
                selectedSubtopic = null
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

       // Divider(color = Color.Gray, thickness = 0.5.dp)

        when (selectedCategory) {

            is MatchCategory.MoodCategory -> MoodSelector2(
                moods = (selectedCategory as MatchCategory.MoodCategory).moods,
                selected = selectedMood,
                onSelect = { selectedMood = it }
            )

            else -> SubtopicSelector(
                subtopics = selectedCategory.subtopics,
                selected = selectedSubtopic,
                onSelect = { selectedSubtopic = it }
            )
        }

    }
}


@Composable
fun GenderSelection() {
    val listOfTypes= listOf("Male","Female","Others","Random")
    var selected by remember { mutableStateOf(listOfTypes[0]) }
    Text(text = "Looking For:", fontFamily = Constants.FONT_LIGHT, fontSize = 14.sp,color= Color.White)
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {

        listOfTypes.forEach {
            Card(Modifier
                .wrapContentSize()
                .clickable { selected = it }, backgroundColor = if (selected==it) floatingActionBtnColor else Color.Gray, contentColor = Color.White) {
                Text(text = it, fontFamily = Constants.FONT_LIGHT, fontSize = 14.sp,color= if (selected==it)Color.White else Color.DarkGray, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}

@Composable
fun InterestSelector(categories: List<MatchCategory>,
                     selectedCategory: MatchCategory,
                     onCategorySelected: (MatchCategory) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Matching on:",
            fontFamily = Constants.FONT_LIGHT,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp), modifier = Modifier
                .clickable { expanded = true }
                .clip(shape = RoundedCornerShape(12.dp))
                .background(floatingActionBtnColor)
                .wrapContentWidth()
                .height(30.dp)) {
                Text(
                    text = selectedCategory.title,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
                Image(painter=painterResource(R.drawable.arrow_down), contentDescription = "", modifier = Modifier.size(24.dp), colorFilter = ColorFilter.tint(color = Color.White))
            }


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(120.dp)
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onCategorySelected(category)
                    },contentPadding=PaddingValues(horizontal = 4.dp)) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = category.title,
                                    fontFamily = Constants.ROBOTO_CONDENSED,
                                    fontSize = 16.sp,
                                    lineHeight = 12.sp
                                )
//                                Text(
//                                    text = item.second.toString(),
//                                    fontFamily = Constants.ROBOTO_CONDENSED,
//                                    fontSize = 14.sp,
//                                    lineHeight = 12.sp
//                                )
                            }
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp), thickness = 0.25.dp, color = Color.Gray)
                        }
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
    val name:String,
    val color: Color=Color(0xFFFFFFFF)
)
@Composable
fun DuelOptions(isVisible:Boolean, onCloseClicked:()->Unit, onNextClicked:()-> Unit, onScreenClicked:()->Unit) {
    val listOfImages= listOf(
        DualScreenOptions(R.drawable.close,"end", color =  Color.Unspecified),
        DualScreenOptions(R.drawable.add,"+friend"),
        DualScreenOptions(R.drawable.flip_camera_android_24,"flip"),
       // DualScreenOptions(R.drawable.add_photo_by_camera,"screenshot"),
        DualScreenOptions(R.drawable.next_person,"next", color = Color.Unspecified)
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
            Column(modifier = Modifier.wrapContentSize().padding(end = 20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id =it.id), contentDescription = "", modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable {
                        if (it.name == "end") {
                            onCloseClicked()
                        }
                        if(it.name=="next"){
                            onNextClicked()
                        }
                    }.size(36.dp), colorFilter = if (it.color == Color.Unspecified) null else ColorFilter.tint(it.color))
                Text(text = it.name, fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp,color=Color.White)
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
fun PrivatePublicSwitch(isSelected: Boolean=true, onClick:()-> Unit={}) {

    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        Switch(isSelected, onCheckedChange = {onClick()}, modifier = Modifier.size(28.dp), colors = SwitchDefaults.colors(checkedThumbColor = floatingActionBtnColor, uncheckedThumbColor = Constants.HOME_TOP_BAR_COLOR, checkedTrackColor = Color(
            0xFFFFFFFF
        ), uncheckedTrackColor = Color.LightGray))
        Text("private", fontFamily = Constants.ROBOTO_CONDENSED, fontSize = 9.sp, lineHeight = 12.sp, color = Color.LightGray)
    }
}

@Composable
fun DuelCameraEdit(title: String,selectedIcon:Int, unSelectedIcon: Int=0, tint:Boolean=true, size:Int=28, isSelected: Boolean=true, onClick:()-> Unit={}) {

    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = if (isSelected) selectedIcon else unSelectedIcon),
            contentDescription = "",
            modifier = Modifier
                .size(size.dp)
                .clickable { onClick() },
            colorFilter = if (isSelected && tint) ColorFilter.tint(floatingActionBtnColor) else null
        )
    Text(title, fontFamily = Constants.ROBOTO_CONDENSED, fontSize = 9.sp, lineHeight = 12.sp, color = Color.LightGray.copy(alpha = 0.7f))
 }
}


@Composable
fun MoodSelector2(
    moods: List<MoodItem>,
    selected: MoodItem?,
    onSelect: (MoodItem) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        moods.forEach { mood ->
            val isSelected = mood == selected
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
//                        if (isSelected) Color.White else Color(
//                            android.graphics.Color.parseColor(
//                                mood.colorHex
//                            )
//                        ).copy(alpha = 0.6f)
                        if (isSelected) Color.White else Color.DarkGray            )
                    .clickable { onSelect(mood) }
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
               // MoodBall(mood.colorHex)
                //Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = mood.label,
                    color = if (isSelected) Color.Black else Color.White,
                    fontSize = 14.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}
@Composable
fun MoodBall(colorHex: String, size: Dp = 16.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(android.graphics.Color.parseColor(colorHex)).copy(alpha = 0.7f))
    )
}



val moodList = listOf(
    MoodItem("#FFD93D", "Happy"),      // Warm Yellow
    MoodItem("#4D8BFF", "Sad"),        // Soft Blue
    MoodItem("#FF3B6B", "In Love"),    // Vibrant Pink/Red
    MoodItem("#7DF4C4", "Cool"),       // Mint Green
    MoodItem("#C39EFF", "Curious"),    // Lavender Purple
    MoodItem("#A0A4A8", "Tired"),      // Muted Grey
    MoodItem("#FF5F5F", "Angry"),
    MoodItem("#FFB14A", "Hype"), // Punch Red
    MoodItem("#9BA6B2", "Bored"),      // Neutral Blue-Grey
    MoodItem("#6EE7B7", "Chill")      // Calm Green
           // Energetic Orange
)

