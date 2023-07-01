package com.example.finalapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenUI(navController: NavController= NavController(LocalContext.current)){
    val color=0xFFF7F7FA
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        ) {
            ProfileItem(color )

        }
        
    }


@Composable
fun ProfileItem(color: Long){

    Card(modifier = Modifier
        .height(420.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
//            Box(
//                contentAlignment = Alignment.CenterStart,
//                modifier = Modifier
//                    .background(color = Color(0xFFDEF30B))
//                    .padding(start = 8.dp)
//                    .fillMaxWidth()
//                    .height(30.dp)) {
//                Text(text = "Location : Near by", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//            }

            Row() {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .background(color = Color.White)
                ) {
                    Image(
                        painter = painterResource(id =R.drawable.hd_girl ),
                        contentDescription ="" ,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(color))) {
                    Box(modifier = Modifier
                        .background(color = Color(color ))
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(start = 8.dp, 4.dp, 4.dp, 4.dp), contentAlignment = Alignment.CenterStart) {
                        Text(text = "SURAJ_SINGH", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier
                        .background(color = Color(color))
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(start = 8.dp, 4.dp, 4.dp, 4.dp), contentAlignment = Alignment.CenterStart) {
                        Row() {
                            Text(text = "Posts:", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "157", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.Black)
                        }


                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier
                        .background(color = Color(color))
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(start = 8.dp, 4.dp, 4.dp, 4.dp), contentAlignment = Alignment.CenterStart) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Active Post:", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.Black)
                            Text(text = "Get me a movie ticket",color = Color.Black)

                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier
                        .background(color = Color(color))
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(start = 8.dp, 4.dp, 4.dp, 4.dp), contentAlignment = Alignment.CenterStart) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "OFFER:", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.Black)
                            Row() {
                                Column(modifier = Modifier
                                    .wrapContentSize()
                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(painterResource(id =R.drawable.chat_24 ),
                                            contentDescription ="Chat icon" ,
                                            tint = Color(0xFFEB410B),
                                            modifier = Modifier.size(30.dp) )
                                    }
                                    Text(text = "Chat", fontSize = 13.sp,fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier
                                    .wrapContentSize()
                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(painterResource(id =R.drawable.video_24 ),
                                            contentDescription ="Video icon" ,
                                            tint = Color(0xFF0A1A72),
                                            modifier = Modifier.size(37.dp) )
                                    }
                                    Text(text = "Video", fontSize = 13.sp,fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier
                                    .wrapContentSize()
                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(painterResource(id =R.drawable.call_24 ),
                                            contentDescription ="Audio icon" ,
                                            tint = Color(0xFF08B10E),
                                            modifier = Modifier.size(35.dp) )
                                    }
                                    Text(text = "Audio", fontSize = 13.sp,fontWeight = FontWeight.Bold)
                                }


                            }
                            Row() {
                                Column(modifier = Modifier
                                    .wrapContentSize()
                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(painterResource(id =R.drawable.coffee_24 ),
                                            contentDescription ="Coffee icon" ,
                                            tint = Color(0xFF000000),
                                            modifier = Modifier.size(35.dp) )
                                    }
                                     Text(text = "Coffee", fontSize = 13.sp,fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier
                                    .wrapContentSize()
                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            painterResource(id =R.drawable.personal_24 ),
                                            contentDescription ="chat icon" ,
                                            tint = Color(0xFF990707),
                                            modifier = Modifier.size(35.dp)
                                        )
                                    }
                                    Text(text = "Personal", fontSize = 11.sp,fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier
                                    .wrapContentSize()
                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            painterResource(id =R.drawable.food_24 ),
                                            contentDescription ="Food icon" ,
                                            tint = Color(0xFFFFBD20),
                                            modifier = Modifier.size(35.dp)
                                        )
                                    }
                                    Text(text = "Lunch", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }


                            }




                        }
                    }

                    Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
                       Button(
                           onClick = { /*TODO*/ },
                           shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 6.dp),
                           colors = ButtonDefaults.buttonColors(
                               containerColor = Color(0xFFAA0CC5),
                               contentColor = Color.Black),
                           modifier = Modifier
                               .fillMaxWidth()
                               .wrapContentHeight()
                               .padding(start = 4.dp)
                       ) {
                           Text(text = "CONNECT", fontSize = 16.sp)
                       }

                       }
                }
            }
            
        }
        
    }

}