package com.example.finalapp.screens

import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import kotlinx.coroutines.delay

fun LazyListState.isScrolledToTheEnd() : Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(navController: NavHostController){
    val buttonVisible=remember { mutableStateOf(true)};
    val listState = rememberLazyListState()
    var messages by remember {
        mutableStateOf(emptyList<String>())
    }
    LaunchedEffect(true) {
        while (true) {
            delay(1000)
            messages = messages + "Test"
        }
    }

    LaunchedEffect(messages.size) {
        if (!listState.isScrolledToTheEnd()) {
            val itmIndex = listState.layoutInfo.totalItemsCount - 1
            if (itmIndex >= 0) {
                val lastItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastItem?.let {
                    listState.animateScrollToItem(itmIndex, it.size + it.offset)
                }
            }
        }
    }
    Scaffold(
        topBar = { HomeTopBar(title = "Chat", navController , actionIcon = false, icon = R.drawable.settings)},
        bottomBar = {BottomBar(navController = navController, state = buttonVisible, modifier = Modifier.height(45.dp))})
    {
        LazyColumn(modifier = Modifier.padding(it), state = listState ){
            items(messages){message->
                MessageItem(msg =message)

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