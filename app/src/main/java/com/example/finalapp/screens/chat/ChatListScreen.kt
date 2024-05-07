package com.example.finalapp.screens.chat



import android.annotation.SuppressLint
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.database.Chat
import com.example.finalapp.datastore.StoreUserData
import com.example.finalapp.model.ChatUser
import com.example.finalapp.screens.HomeTopBar
import kotlinx.coroutines.launch


val users:List<ChatUser> = listOf (
    ChatUser("Suraj","1"),
    ChatUser("Nishant","2"),
    ChatUser("Nitish","3"),
    ChatUser("Muskan","4"),
    ChatUser("Bhomi","5"),
    ChatUser("Supriya","6")

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
                UserItem(navController , name=users.name,userNumber = users.number, lastMessage = "hello")

            }
        }
    }
}

@Composable
fun UserItem(navController: NavHostController,name: String,userNumber:String,lastMessage:String){

    Card(modifier = Modifier
        .padding(start = 8.dp, end = 8.dp, top = 10.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable { navController.navigate("singleChat/$userNumber") },
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
                    Text(text = userNumber, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
                    Text(text = "08:38",style = MaterialTheme.typography.labelSmall, fontSize = 12.sp)
                }
                Row() {
                    Text(text = if(lastMessage.isNotEmpty()) lastMessage else "hello",style = MaterialTheme.typography.titleMedium, fontSize = 12.sp)
                }



            }

        }

    }
}

/*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(userNumber: String?,navController: NavHostController,chatViewModel: ChatViewModel) {
    val context= LocalContext.current
    val viewModel = viewModel<ChatViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    var key by remember {
        mutableStateOf(0)
    }
    var inputText by remember { mutableStateOf("") }
    val datastore=StoreUserData(context )
//    val number by  datastore.getUserNumber.collectAsState("")
//    LaunchedEffect(key1 = true){
//        datastore.saveUserNumber("+9179034")
//    }




    Scaffold(topBar = { HomeTopBar(
        title = userNumber!!,
        navController = navController,
        navIcon =false ,
        actionIcon =false,
        icon = R.drawable.profile_image_1
    )
    }) {

        // Auto-scroll to the bottom when messages list is updated
        LaunchedEffect(messages) {
            listState.animateScrollToItem(messages.size)
        }
        val scope = rememberCoroutineScope()
        if (key == 1) {

            LaunchedEffect(key1 = true) {
                scope.launch {
                    chatViewModel.saveChatToDB(
                        Chat(
                            sentTo = userNumber.toString(),
                            message = inputText,
                            received = "false",
                            sent = "Single Tick",
                            seen = "false",
                            timeStamp = System.currentTimeMillis()
                        )
                    )
                }
                key=0
            }
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
                    key=1
                    viewModel.sendMessage(userNumber!!,inputText)
                    viewModel.messages.value
                    inputText = ""
                }) {
                    Text("Send")
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
*/