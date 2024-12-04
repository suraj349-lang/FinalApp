package com.example.finalapp.screens._6chat



import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.model.ChatUser
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.statusAndTopAppBarColor


val users:List<ChatUser> = listOf (
    ChatUser("Suraj2","+917250260100"),
    ChatUser("Suraj","+916376099670"),
    ChatUser("sudha","+917367984901")

)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListScreen(navController: NavHostController) {
    val listOfUsers: List<ChatUser> by remember {
        mutableStateOf(users)
    }
    Scaffold(topBar = {
        ChatTopBar(
            title = "Chats",
            navController = navController
        )
    }) {


        LazyColumn(modifier = Modifier.padding(it)){
            itemsIndexed(listOfUsers){ i,users->
                UserItem(navController , name=users.name,userNumber = users.number, lastMessage = "hello")

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(title: String, navController: NavHostController) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = statusAndTopAppBarColor
            ),
            title = {
                Text(
                    title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top=8.dp), color = Color( 0xFF000000), style = MaterialTheme.typography.titleMedium
                )
            },
            navigationIcon = {

                    Image(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable { navController.navigate(SCREENS.HOME.route) }
                            .padding(top = 6.dp)
                            .size(40.dp)
                    )
            }, actions = {

            }
        )
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