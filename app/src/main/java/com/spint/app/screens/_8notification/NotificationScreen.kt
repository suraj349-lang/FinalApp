package com.spint.app.screens._8notification

import BottomBar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.spint.app.R
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.NotificationViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState


@Composable
fun NotificationScreenUI(navController: NavHostController, viewModel:NotificationViewModel) {
    val buttonVisible = remember { mutableStateOf(false) };
    val notifications by viewModel.notifications.collectAsState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isRefreshing){
        if (isRefreshing){
            viewModel.loadNotifications()
            isRefreshing=false
        }
    }


    SwipeRefresh(state = SwipeRefreshState(isRefreshing), onRefresh = { isRefreshing=true }) {
        Scaffold(
            topBar = { NotificationTopBar(onDeleteAllClicked = {viewModel.clearAll()}) { navController.navigateUp() } },
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
            Surface(
                Modifier
                    .fillMaxSize()
                    .padding(it),
                color = Color(0xFFEEEBE4)
            ) {
                //   ChatScreenUIFCM()


                LazyColumn {
                    items(notifications) { notification ->
                        NotificationItem(notification, onMarkRead = {
                            viewModel.markAsRead(notification.id)
                        }, onDelete = {
                            viewModel.delete(notification.id)
                        })
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopBar(title:String="Notifications",onDeleteAllClicked:()->Unit,onBackClicked:()->Unit) {
    TopAppBar(
        title = {Text(title, fontFamily = Constants.FONT_MEDIUM, color = Color.Black)},
        actions = { Image(painter = painterResource(id = R.drawable.delete), contentDescription ="", modifier = Modifier.padding(end=16.dp).size(24.dp).clickable { onDeleteAllClicked() } )},
        navigationIcon = { Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription ="", modifier = Modifier.clickable { onBackClicked() } )},
        modifier = Modifier
            .shadow(elevation = 60.dp,)
            .zIndex(2f),
       // colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFEEEBE4)),
    )
}
