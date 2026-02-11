package com.spint.app.screens._4profile

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.spint.app.R
import com.spint.app.screens.dialogBox.DropProfileDialog
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.viewmodels.HomeViewModel
import com.spint.app.viewmodels.ImageUploadViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PublicProfileScreenUI(navController: NavHostController, authViewModel:AuthViewModel, homeViewModel:HomeViewModel, imageUploadViewModel:ImageUploadViewModel) {
    var showCustomDialog by remember {
        mutableStateOf(false)
    }
    if (showCustomDialog) {
        DropProfileDialog(authViewModel ,homeViewModel , imageUploadViewModel ,navController ) { showCustomDialog = !showCustomDialog }
        Log.d("Suraj", "Profile Screen : Drop Profile ")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = floatingActionBtnColor) //Color(0xFFE4EE05)
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, Color(0xFF1B1A1A)
                            ), startY = 0f, endY = 600f
                        )
                    )) {
                    Card(modifier = Modifier
                        .padding(4.dp)
                        .size(25.dp)
                        .clickable { navController.navigateUp() }
                        .align(Alignment.TopStart),
                        backgroundColor = Color.Black.copy(alpha = 0.2f),
                        shape = CircleShape) {
                        Image(
                            painterResource(id = R.drawable.back),
                            contentDescription = "",
                            contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.White)
                        )

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Card(
                                    modifier = Modifier.size(60.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, color = Color.Black),
                                    elevation = 20.dp
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.qr),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )


                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = "Suraj Singh",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = "singhSuraj_94",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )

                                }

                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()) {
                                Card(modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(30.dp), backgroundColor = Color(0xFFFFFFFF).copy(alpha = 0.2f),border = BorderStroke(width = 2.dp, color = Color.White),shape = RoundedCornerShape(30.dp)) {
                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "My account", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Card(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .height(30.dp), backgroundColor = Color.White.copy(alpha = 0.5f),shape = RoundedCornerShape(30.dp)) {
                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Public Profile", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                }

                            }


                        }

                    }

                }
            }
            Column(modifier = Modifier.padding(start = 16.dp,end=16.dp,top=10.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                MyLiveEvents(){
////                    navController.navigate(SCREENS.CREATE_EVENT.route)
//                }
//                RecentDrops(response.data) {
//                    showCustomDialog=!showCustomDialog
//                }
                UserStats()
            }

        }

    }

}


