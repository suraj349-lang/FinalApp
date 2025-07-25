package com.example.finalapp.screens._8notification

import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TimeLineScreenUI(navController: NavHostController) {
    val buttonVisible = remember { mutableStateOf(false) };

    Scaffold(
        topBar = { NotificationTopBar(){navController.navigateUp()} },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonVisible,
                modifier = Modifier.height(45.dp),
                containerColor = Constants.HOME_BOTTOM_BAR_COLOR,
                inactiveIconColor = Constants.BOTTOM_BAR_INACTIVE_ICON_COLOR,
                highlightedTextColor = Constants.BOTTOM_BAR_ACTIVE_TEXT_COLOR,

            ) {}
        }
    ) {
        Surface(Modifier.fillMaxSize().padding(it)) {
            //   ChatScreenUIFCM()
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                items(dummyNotifications) { notification ->
                    NotificationCard(notification) {
                       // viewModel.markAsRead(notification.id)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopBar(title:String="Notifications",onBackClicked:()->Unit) {
    TopAppBar(
        title = {Text(title, fontFamily = Constants.FONT_MEDIUM, color = Color.Black)},
        actions = {},
        navigationIcon = { Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription ="", modifier = Modifier.clickable { onBackClicked() } )},
        modifier = Modifier
            .shadow(elevation = 60.dp,)
            .zIndex(2f)
    )
}

val dummyNotifications = listOf(
    TimeLineItem(
        id = "1",
        type = "message",
        title = "New message from Aman",
        description = "Hey! Are you joining the event tonight?",
        timestamp = System.currentTimeMillis() - 5 * 60 * 1000, // 5 min ago
        isRead = false
    ),
    TimeLineItem(
        id = "2",
        type = "event_created",
        title = "You created an event",
        description = "Your event 'Cricket at Sunset Park' is live!",
        timestamp = System.currentTimeMillis() - 60 * 60 * 1000, // 1 hour ago
        isRead = true
    ),
    TimeLineItem(
        id = "3",
        type = "ping_upvoted",
        title = "Your ping got 5 upvotes!",
        description = "Your ping 'Need a study partner' received 5 new upvotes.",
        timestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000, // 2 hours ago
        isRead = false
    ),
    TimeLineItem(
        id = "4",
        type = "joined_event",
        title = "2 people joined your event",
        description = "Your event 'Book Club Meetup' has 2 new members.",
        timestamp = System.currentTimeMillis() - 3 * 60 * 60 * 1000,
        isRead = true
    ),
    TimeLineItem(
        id = "5",
        type = "comment",
        title = "New comment on your ping",
        description = "“I'm interested in this too!”",
        timestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000,
        isRead = false
    )
)
