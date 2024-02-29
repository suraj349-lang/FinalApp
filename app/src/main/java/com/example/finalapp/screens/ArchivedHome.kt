package com.example.finalapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenUI2(navController: NavController= NavController(LocalContext.current)){
    val items=(1 .. 100).map {
        ListItem(
            height = Random.nextInt(100,300).dp,
            color = Color(Random.nextLong(0xFFFFFFFF)).copy(alpha = 1f)

        )
    }
    Scaffold(topBar = {
        TopAppBar(

            title ={ Text(text = "title")},
            actions = {
                Icon(imageVector = Icons.Default.Add, contentDescription ="" )
            },
            navigationIcon = {
                Icon(imageVector =Icons.Default.Person , contentDescription ="" )
            }

        )
    }) {

            Column(modifier = Modifier
                .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(60.dp))


                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        RandomColorBox(item = item)
                    }


                }
            }
        }

    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(title:String) {
    TopAppBar(

        title ={ Text(text = title)},
        actions = {
            Icon(imageVector = Icons.Default.Add, contentDescription ="" )
        },
        navigationIcon = {
            Icon(imageVector =Icons.Default.Person , contentDescription ="" )
        }

    )
}
data class ListItem(
    val height:Dp,
    val color: Color
)

@Composable
fun RandomColorBox(item:ListItem){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(item.height)
        .clip(RoundedCornerShape(10.dp))
        .background(item.color))

}
@Composable
fun ProfileItem(color: Long){

    Card(modifier = Modifier
        .height(300.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
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
                        painter = painterResource(id =R.drawable.tree ),
                        contentDescription ="" ,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(color))) {
                    Box(modifier = Modifier
                        .background(color = Color(color))
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
//                            Row() {
//                                Column(modifier = Modifier
//                                    .wrapContentSize()
//                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                                    IconButton(onClick = { /*TODO*/ }) {
//                                        Icon(painterResource(id =R.drawable.chat_24 ),
//                                            contentDescription ="Chat icon" ,
//                                            tint = Color(0xFFEB410B),
//                                            modifier = Modifier.size(30.dp) )
//                                    }
//                                    Text(text = "Chat", fontSize = 13.sp,fontWeight = FontWeight.Bold)
//                                }
//                                Column(modifier = Modifier
//                                    .wrapContentSize()
//                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                                    IconButton(onClick = { /*TODO*/ }) {
//                                        Icon(painterResource(id =R.drawable.video_24 ),
//                                            contentDescription ="Video icon" ,
//                                            tint = Color(0xFF0A1A72),
//                                            modifier = Modifier.size(37.dp) )
//                                    }
//                                    Text(text = "Video", fontSize = 13.sp,fontWeight = FontWeight.Bold)
//                                }
//                                Column(modifier = Modifier
//                                    .wrapContentSize()
//                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                                    IconButton(onClick = { /*TODO*/ }) {
//                                        Icon(painterResource(id =R.drawable.call_24 ),
//                                            contentDescription ="Audio icon" ,
//                                            tint = Color(0xFF08B10E),
//                                            modifier = Modifier.size(35.dp) )
//                                    }
//                                    Text(text = "Audio", fontSize = 13.sp,fontWeight = FontWeight.Bold)
//                                }
//
//
//                            }
                            Row() {
                                Column(modifier = Modifier
                                    .wrapContentSize()
                                    .padding(0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(painterResource(id =R.drawable.coffeecup ),
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
                                            painterResource(id =R.drawable.heart ),
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
                                            painterResource(id =R.drawable.dinner ),
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
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PostItem2(){
    var key by remember { mutableStateOf(false) }
    var icon=if(key)
        painterResource(id = R.drawable.heart)
    else
        painterResource(id = R.drawable.dinner)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF),
        ),
        elevation = CardDefaults.cardElevation(10.dp)

    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Card(modifier= Modifier
                .padding(start = 6.dp, top = 6.dp, end = 6.dp, bottom = 0.dp)
                .fillMaxWidth()
                .height(280.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(6.dp)) {

                Image(
                    painter = painterResource(id =R.drawable.tree ),
                    contentDescription ="",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize() )

            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF)
                    ),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Text(
                        text = "Dedicate me a song",
                        fontSize =20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(4.dp),
                        color = Color(0xFFE4420E)
                    )
                }
            }
            Text(text = "Offer:",color= Color(0xFF2A7BBB), fontSize = 14.sp,fontWeight = FontWeight.Bold, modifier = Modifier.padding(start =4.dp, top = 4.dp))

                Card() {
                    Row(modifier=Modifier.padding(8.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                    Icon(
                        painter = painterResource(id = R.drawable.coffeecup),
                        contentDescription ="Coffee",
                        tint = Color(0xFF0E0D0C),
                        modifier = Modifier.size(25.dp)

                    )
                    Text(text = "at Starbucks", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(start = 2.dp))
                }

            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .padding(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF),
                    ), elevation = CardDefaults.cardElevation(2.dp)) {
                    Icon(painterResource(id = R.drawable.reject_24), contentDescription ="Like Button",tint= Color(0xFF817676), modifier = Modifier.size(40.dp) )
                }
                Spacer(modifier = Modifier.width(32.dp))
                Card(
                    modifier = Modifier
                        .padding(2.dp),
                    onClick={key=!key},
                    colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFFFFF)), elevation = CardDefaults.cardElevation(2.dp)) {
                    Icon(painter = icon , contentDescription ="Like Button",tint= Color(0xFF990707), modifier = Modifier.size(40.dp) )

                }


            }



        }


    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PostItem3(){
    var key by remember { mutableStateOf(false) }
    var icon=if(key)
        painterResource(id = R.drawable.heart)
    else
        painterResource(id = R.drawable.heart)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF),
        ),
        elevation = CardDefaults.cardElevation(10.dp)

    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Card(modifier= Modifier
                .padding(start = 6.dp, top = 6.dp, end = 6.dp, bottom = 0.dp)
                .fillMaxWidth()
                .height(280.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(6.dp)) {

                Image(
                    painter = painterResource(id =R.drawable.tree ),
                    contentDescription ="",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize() )

            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF)
                    ),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Text(
                        text = "Dance for me",
                        fontSize =20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(4.dp),
                        color = Color(0xFFE4420E)
                    )
                }
            }
            Text(text = "Offer:",color= Color(0xFF2A7BBB), fontSize = 14.sp,fontWeight = FontWeight.Bold, modifier = Modifier.padding(start =4.dp, top = 4.dp))

            Card() {
                Row(modifier=Modifier.padding(8.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                    Icon(
                        painter = painterResource(id = R.drawable.coffeecup),
                        contentDescription ="Coffee",
                        tint = Color(0xFF0E0D0C),
                        modifier = Modifier.size(25.dp)

                    )
                    Text(text = "at Cafeteria", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(start = 2.dp))
                }

            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .padding(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF),
                    ), elevation = CardDefaults.cardElevation(2.dp)) {
                    Icon(painterResource(id = R.drawable.reject_24), contentDescription ="Like Button",tint= Color(0xFF817676), modifier = Modifier.size(40.dp) )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {key=!key },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
                    modifier=Modifier.wrapContentSize(), shape = CircleShape

                ) {
                    Icon(painter = icon , contentDescription ="Like Button",tint= Color(0xFF990707), modifier = Modifier.size(40.dp) )

                }


            }



        }


    }}