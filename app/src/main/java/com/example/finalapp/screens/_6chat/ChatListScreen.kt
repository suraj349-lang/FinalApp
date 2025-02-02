package com.example.finalapp.screens._6chat



import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.model.ChatUser
import com.example.finalapp.navigation.SCREENS


val users:List<ChatUser> = listOf (
    ChatUser("Tedha",R.drawable.girl,"+917250260100"),
    ChatUser("Bahubali",R.drawable.profile_image_2,"+917250260100"),
    ChatUser("Sakshi",R.drawable.profile_image_1,"+917250260100"),
    ChatUser("Supriya",R.drawable.profile_image_3,"+917250260100"),
    ChatUser("Sakshi Vashishth",R.drawable.girl,"+917250260100"),
    ChatUser("Manthan",R.drawable.girl,"+917250260100"),
    ChatUser("Tedha",R.drawable.girl,"+917250260100"),
    ChatUser("Bahubali",R.drawable.profile_image_2,"+917250260100"),
    ChatUser("Sakshi",R.drawable.profile_image_1,"+917250260100"),
    ChatUser("Supriya",R.drawable.profile_image_3,"+917250260100"),
    ChatUser("Sakshi Vashishth",R.drawable.girl,"+917250260100"),
    ChatUser("Manthan",R.drawable.girl,"+917250260100"),

)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun ChatListScreen(navController: NavHostController= NavHostController(LocalContext.current)) {
    val listOfUsers: List<ChatUser> by remember {
        mutableStateOf(users)
    }
    val chatRowListItems=listOf("All","Unread","Unreplied")
    val buttonsVisible = remember { mutableStateOf(true) }
    Scaffold(topBar = {
        ChatTopBar(
            title = "Chat",
            navController = navController
        )
    }, bottomBar = {BottomBar(navController = navController, state =buttonsVisible )}
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            LazyRow(modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .height(40.dp)) {
                items(chatRowListItems){item->
                    ChatRowItem(item)
                }
            }
            LazyColumn(modifier = Modifier) {
                itemsIndexed(listOfUsers) { i, user ->
                    UserItem(navController, user)
                    Divider(modifier = Modifier.fillMaxWidth(), color = Color(0xFFF1EAEA))

                }
            }
        }
    }
}

@Composable
fun ChatRowItem(item: String) {
    Card(modifier = Modifier
        .wrapContentSize()
        .padding(8.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
        Column(
            Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =item,
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(title: String, navController: NavHostController) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            title = {
                Text(
                    title,textAlign= TextAlign.Center, modifier = Modifier.fillMaxWidth(0.6f), color = Color( 0xFF000000), fontSize = 20.sp
                )
            },
            navigationIcon = {
                Row(modifier = Modifier.fillMaxWidth(0.2f)) {
                    Card(
                        modifier = Modifier.size(30.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Image(
                            painterResource(id = R.drawable.person_new_filled),
                            colorFilter = ColorFilter.tint(Color.DarkGray),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable { navController.navigate(SCREENS.PROFILE.route) }
                                .padding(4.dp)

                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Card(
                        modifier = Modifier.size(30.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Image(
                            painterResource(id = R.drawable.search_new_filled),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.DarkGray),
                            modifier = Modifier
                                .clickable { navController.navigate(SCREENS.PROFILE.route) }
                                .padding(8.dp)

                        )
                    }
                }
            }, actions = {
                Card(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Image(
                        painterResource(id = R.drawable.menu),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.DarkGray),
                        modifier = Modifier
                            .clickable { navController.navigate(SCREENS.PROFILE.route) }
                            .padding(8.dp)

                    )
                }

            }
        )
    }

@Composable
fun UserItem(navController: NavHostController, user: ChatUser){

    Card(modifier = Modifier
        .padding(start = 8.dp, end = 8.dp)
        .fillMaxWidth()
        .height(60.dp)
        .clickable {
            navController.navigate("singleChat/${user.number}")
        },
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
            Card(modifier = Modifier.size(50.dp), shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                Image(painter = painterResource(id = user.image), contentDescription = "",modifier = Modifier
                    .padding(2.dp)
                    .size(40.dp)
                    .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = user.name, fontSize = 16.sp, color = Color.Black)
                    Text(text = "08:38", fontSize = 12.sp, color = Color.LightGray)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text(text =  "hello",style = MaterialTheme.typography.titleMedium, fontSize = 8.sp,color= Color.DarkGray)
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