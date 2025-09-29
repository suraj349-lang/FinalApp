package com.example.finalapp.screens._1home.commonUI

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.enums.ImageUploadScreens
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.DropProfileDialog
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.ui.theme.homeTopBarIconsColor
import com.example.finalapp.ui.theme.statusBarColor
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.APP_NAME_FONT
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.viewmodels.ImageUploadViewModel
import kotlinx.coroutines.launch


@Composable
fun HomeError(eventsViewModel: EventsViewModel){
    var retry by remember {
        mutableStateOf(false)
    }
    if(retry) {
        retry=false;
        RetryCall(eventsViewModel = eventsViewModel)
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.oops), contentDescription = "oops")
        Text(text = "Error loading Profiles !", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color(
            0xFFE91E63
        )
        )
        Button(onClick = {retry=true}) {
            Text(text = "Retry")

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    backgroundColor:Color = Color.DarkGray,
    iconAndTextColor :Color=statusBarColor,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    titleColor:Color = statusBarColor,
    navController: NavHostController,
    navIcon: Boolean,
    actionIcon: Boolean,
    icon: Int? = null,
    onQRClicked: () -> Unit = {}
){
    TopAppBar(
        modifier=
        Modifier
            .shadow(elevation = 4.dp)
            .zIndex(6f)
            .fillMaxWidth()
            .height(40.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        ),
        scrollBehavior = scrollBehavior,
        title = {
            Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                title,
                fontSize = 20.sp,
            //    fontWeight=FontWeight.SemiBold,
                modifier = Modifier, color = titleColor, fontFamily = APP_NAME_FONT //Constants.FONT_MEDIUM,
            )
            }
        },
        actions = {
            if(actionIcon) {
                Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Image(painter = painterResource(id = R.drawable.new_qr),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color = iconAndTextColor),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onQRClicked()
                            })
                    Image(
                        painter = painterResource(id = R.drawable.notification_new),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color = iconAndTextColor),
                        modifier = Modifier
                            .clickable { navController.navigate(SCREENS.NOTIFICATIONS.route) }
                            .size(20.dp)
                    )
                    icon?.let { painterResource(id = it) }?.let {
                        Image(painter = it,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(color = iconAndTextColor),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    navController.navigate(SCREENS.CHAT_LIST.route)
                                })
                    }
                }
            }

        }
    )
}



@Composable
fun RetryCall(eventsViewModel: EventsViewModel){
    val scope= rememberCoroutineScope()
    scope.launch {
        eventsViewModel.getAllEvents()
    }

}

@Composable
fun LoadingIndicator(){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
        {
            LinearProgressIndicator(color = Color(0xFFE9EEE3), trackColor = Color(0xFF284705))
            // Image(painter = painterResource(id = R.drawable.loading), contentDescription ="" )
            // CircularProgressIndicator(color = Color(0xFFE9EEE3), trackColor = Color(0xFF284705), strokeWidth = 4.dp, strokeCap = StrokeCap.Square, modifier = Modifier.size(70.dp))


        }


    }

}

@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true)
@Composable
fun HomeLoading(padding: PaddingValues = PaddingValues(65.dp)){
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.78f
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.profileloading),
            contentDescription = "",
            modifier = Modifier
                .background(brush = ShimmerEffect())
                .fillMaxHeight()
                .width(widthInDp * 0.99f)
            , contentScale = ContentScale.FillBounds
        )
    }
}
@Composable
fun ShimmerEffect(showShimmer: Boolean = true, targetValue: Float = 10000f): Brush {
    return if (showShimmer) {
        // Colors for the shimmer effect
        val shimmerColors = listOf(
            Color.Gray.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.01f),
            Color.Gray.copy(alpha = 0.6f),
        )

        // Start the animation transition
        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        // Return a linear gradient brush
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        // If shimmer is turned off, return a transparent brush
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}


@Composable
fun HomeFloatingActionButton(authViewModel: AuthViewModel, eventsViewModel: EventsViewModel, imageUploadViewModel: ImageUploadViewModel, navController: NavHostController) {
    var showCustomDialog=eventsViewModel.showDropDialog.value

    FloatingActionButton(
        onClick = {
           // showCustomDialog = !showCustomDialog
                  navController.navigate("camerax/${ImageUploadScreens.DROP_PROFILE.screen}")
                  },
        shape= RoundedCornerShape(8.dp),
        modifier = Modifier
            .wrapContentSize()
            .padding(2.dp),
        contentColor = Color.White,
        containerColor =floatingActionBtnColor
    ) {
        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(id = R.drawable.drop_profile_filled_rounded),
                contentDescription = "Add",
                colorFilter = ColorFilter.tint(color=  Color.White),
                modifier= Modifier
                    .padding(top = 4.dp)
                    .size(40.dp)
            )
            Text(text = "Drop", fontSize = 12.sp,modifier = Modifier.padding( top = 0.dp), fontFamily = Constants.FONT_MEDIUM, color = Color.White)
        }
    }
    if (showCustomDialog) {
        DropProfileDialog(
            authViewModel,
            eventsViewModel,
            imageUploadViewModel,
            navController
        ) { showCustomDialog = !showCustomDialog;eventsViewModel.showDropDialog.value = false }
    }

}


@Composable
fun OfferResponseDataAndAction(eventsViewModel: EventsViewModel, navController: NavHostController) {
    val context = LocalContext.current
    Log.d("Data received", "Into the function")
    when (val result = eventsViewModel.premiumCreateEventResponse.value) {
        is RequestState.Success -> {
            eventsViewModel.premiumCreateEventKey.value = 0;
            Log.d("Suraj", result.data.toString())
            Toast.makeText(context, "${result.data}", Toast.LENGTH_SHORT).show()
            navController.navigate(SCREENS.HOME.route) {
                popUpTo(0)
            }

        }

        is RequestState.Error -> {
            Log.d("Data received", "error final found")
            Toast.makeText(context, "$result", Toast.LENGTH_SHORT).show()
        }

        RequestState.Loading -> {
            //   CircularProgressIndicator(color = Color(0xFF1289BE))
        }

        RequestState.Idle -> {

        }

    }


}

