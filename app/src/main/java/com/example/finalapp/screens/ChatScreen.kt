package com.example.finalapp.screens

import BottomBar
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import io.socket.client.Socket
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(mSocket: Socket, authViewModel: AuthViewModel, navController: NavHostController){
    val buttonVisible=remember { mutableStateOf(true)};
    val listState = rememberLazyListState()
    var messages by remember {
        mutableStateOf(emptyList<String>())
    }
    var text by remember {
        mutableStateOf("")
    }
    mSocket.on("+91725026010"){text->
        Log.d("+917250260100 R", text[0].toString())
        messages=messages + text[0].toString()
    }



//    LaunchedEffect(true) {
//        while (true) {
//            delay(1000)
//            messages = messages + "Test"
//        }
//    }

    Scaffold(
        topBar = { HomeTopBar(title = "Chat", navController , actionIcon = false, icon = R.drawable.settings)},
       // bottomBar = {BottomBar(navController = navController, state = buttonVisible, modifier = Modifier.height(45.dp))}
    )
    {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

            LazyColumn(modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth()
                .padding(it), state = listState) {
                items(messages) { message ->
                    MessageItem(msg = message)

                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(value = text, onValueChange = {text=it}, modifier = Modifier
                    .fillMaxWidth(0.85f), shape = RoundedCornerShape(28.dp))
                IconButton(onClick = {
                    mSocket.emit("+917250260100",text)
                    text=""
                    Log.d("+917250260100 S", text)

                }) {
//                    Canvas(modifier = Modifier.fillMaxSize()) {
//                        drawCircle(
//                            color = Color.Green,
//                            radius = 170F,
//                        )
//                    }
                    Icon(painter = painterResource(id = R.drawable.send_24), contentDescription ="",
                        Modifier
                            .size(40.dp)
                            .rotate(-45f) )

                }

            }

        }
    }

}

@Composable
fun MessageItem(msg:String){
    Card(modifier = Modifier
        .fillMaxWidth(0.6f)
        .wrapContentHeight()
        .padding(8.dp)) {
        Text(text =msg, modifier = Modifier.padding(4.dp))


    }
}