package com.example.finalapp.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.Disposable
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.utils.Constants.Constants

@Composable
fun PermissionsUI(authViewModel: AuthViewModel, onGoToAppSettingsClick: () -> Unit){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(brush = Brush.verticalGradient(colors = listOf(
            Color(0xFFFFEB3B), Color(0xFFE9E7E0) //
        )))) {
            Disposable(authViewModel,lifecycleOwner = LocalLifecycleOwner.current)
            Column(
                modifier = Modifier.padding(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(id = R.drawable.tree ),
                    contentDescription ="App Icon" +
                            "",
                    contentScale= ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = Constants.APP_NAME,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                    color = statusAndTopAppBarColor,
                    style = MaterialTheme.typography.titleMedium
                )


            }
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
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.location_icon), contentDescription ="location icon on no permission granted screen", Modifier.size(60.dp) )
                    Text(
                        text = "Please grant permission to access your location inorder to use the app.",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0C0101),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.titleMedium
                    )


                }

            }
            Button(onClick = {onGoToAppSettingsClick()},
                colors =ButtonDefaults.buttonColors(backgroundColor = Color(0xFF033305)  //0xFF033305
                )) {
                Text(text = "Grant Permission", color = Color.White,style = MaterialTheme.typography.titleMedium, fontSize = 18.sp)

            }


        }

    }
}