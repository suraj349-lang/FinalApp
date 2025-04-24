package com.example.finalapp.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalapp.Disposable
import com.example.finalapp.R
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.APP_ICON
import com.example.finalapp.utils.constants.Constants.APP_NAME_FONT
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL


@Composable
fun PermissionsUI(authViewModel: AuthViewModel, onGoToAppSettingsClick: () -> Unit){ Surface(modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(brush = Brush.verticalGradient(colors = listOf(
            Color(0xFFF7F7F7), Color(0xFFE9E7E0) //
        )))) {
            Disposable(authViewModel,lifecycleOwner = LocalLifecycleOwner.current)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(id = APP_ICON ),
                    contentDescription ="App Icon" ,
                    contentScale= ContentScale.Fit,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(40.dp)
                )
                Text(
                    text = Constants.APP_NAME,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                    color = floatingActionBtnColor,
                    fontFamily = APP_NAME_FONT
                )


            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical =16.dp)
                    .wrapContentSize()
                    .background(color = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.location_new),
                    contentDescription = "location icon on no permission granted screen",
                    Modifier
                        .size(60.dp)
                        .padding(top=16.dp)
                )
                Text(
                    text = "App needs to know the location inorder to function",
                    fontWeight = FontWeight.SemiBold,
                    color = floatingActionBtnColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 24.sp,
                    fontFamily = DONGLE_BOLD
                )


            }


            Button(
                onClick = {/*onGoToAppSettingsClick()*/ },
                colors =ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFFFF)  //0xFF033305
                )) {
                Text(text = "GRANT PERMISSION", color = Color(0xFF121212), fontFamily = DONGLE_NORMAL,fontSize = 24.sp, fontWeight = FontWeight.Bold)

            }


        }

    }
}

/*
Box( modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    width = 2.dp, shape = RoundedCornerShape(6.dp), brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFFE91E63),
                            Color(0xFF9C27B0),
                            Color(0xFF2196F3),
                            Color(0xFFFF5722),
                            Color(0xFF043D06)
                        )
                    )
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF0EEE7),
                            Color(0xFFF1EAE8)
                        )
                    )
                )
            ) {
 */