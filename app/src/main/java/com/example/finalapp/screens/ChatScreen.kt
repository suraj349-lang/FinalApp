package com.example.finalapp.screens

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.finalapp.ChatViewModel
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.datastore.StoreUserData
import com.example.finalapp.model.ChatUser
import com.example.finalapp.navigation.SCREENS
import kotlinx.coroutines.launch


val users:List<ChatUser> = listOf (
    ChatUser("Suraj",""),
    ChatUser("Nishant",""),
    ChatUser("Nitish",""),
    ChatUser("Muskan",""),
    ChatUser("Bhomi",""),
    ChatUser("Supriya","")

)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListScreen(navController: NavHostController) {
    val listOfUsers: List<ChatUser> by remember {
        mutableStateOf(users)
    }
    Scaffold(topBar = {
        HomeTopBar(
            title = "Chats",
            navController = navController,
            actionIcon = false,
            navIcon=true,
            icon = R.drawable.profile_image_1
        )
    }) {


    LazyColumn(modifier = Modifier.padding(it)){
        itemsIndexed(listOfUsers){ i,users->
            UserItem(navController , name=users.name,imageUrl = users.profileUrl, lastMessage = "hello")

        }
    }
//        LazyColumn(modifier = Modifier.padding(it)) {
//            items(12) {
//                UserItem(imageUrl = R.drawable.profile_image_1.toString(), lastMessage = "hello")
//            }
//
//        }
   }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserItem(navController: NavHostController,name: String,imageUrl:String,lastMessage:String){

    Card(modifier = Modifier
        .padding(start = 8.dp, end = 8.dp, top = 10.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable { navController.navigate("singleChat/$name") },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFE))
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
//            GlideImage(
//                model =if("${Constants.BASE_URL}${imageUrl}".isNotEmpty()) "" else R.drawable.profile_image_1 ,
//                contentDescription ="",
//                transition= CrossFade,
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .padding(2.dp)
//                    .size(40.dp)
//                    .clip(CircleShape)
//                    .border(1.dp, Color.DarkGray, CircleShape)
//            )
            Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription = "",modifier = Modifier
                .padding(2.dp)
                .size(70.dp)
                .clip(CircleShape)
                .border(1.dp, Color.DarkGray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                   Text(text = name, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
                    Text(text = "08:38",style = MaterialTheme.typography.labelSmall, fontSize = 12.sp)
                }
                Row() {
                    Text(text = if(lastMessage.isNotEmpty()) lastMessage else "hello",style = MaterialTheme.typography.titleMedium, fontSize = 12.sp)
                }



            }

        }

    }
}
@Composable
fun MessageItem(name:String,msg:String,navController: NavHostController){
    Card(modifier = Modifier
        .padding(start = 8.dp, top = 4.dp)
        .wrapContentSize(),
        shape= RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        border = BorderStroke(1.dp, Color.DarkGray)

        ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text =name, modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.titleMedium)
            Text(text =msg, modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.titleMedium)
        }



    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(name: String?,navController: NavHostController) {
    val context= LocalContext.current
    val viewModel = viewModel<ChatViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    val user by remember {
        mutableStateOf(authViewModel.userFromDb)
    }

    val datastore=StoreUserData(context )
    val number by  datastore.getUserNumber.collectAsState("")
    LaunchedEffect(key1 = true){
        datastore.saveUserNumber("+916376099670")
    }



    var inputText by remember { mutableStateOf("") }
    Scaffold(topBar = { HomeTopBar(
        title = name!!,
        navController = navController,
        navIcon =false ,
        actionIcon =false,
        icon = R.drawable.profile_image_1
    )}) {

        // Auto-scroll to the bottom when messages list is updated
        LaunchedEffect(messages) {
            listState.animateScrollToItem(messages.size)
        }

        Column(modifier = Modifier.padding(it)) {
            LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                items(messages) { message ->
                    MessageItem("you", message,navController)
                }
            }


            Row {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp)
                )
                Button(onClick = {
                    viewModel.sendMessage(number!!,inputText)
                    viewModel.messages.value
                    inputText = ""
                }) {
                    Text("Send")
                }
            }
        }
    }
}
