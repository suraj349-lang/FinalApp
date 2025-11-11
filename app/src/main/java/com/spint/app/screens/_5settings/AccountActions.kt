package com.spint.app.screens._5settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spint.app.utils.constants.Constants



@Composable
fun ClearSearchHistory(navController: NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Clear Search History") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Clear Search History",
                    fontSize = 30.sp,
                    fontFamily = Constants.DONGLE_BOLD
                )
            }
        }
    )
}


@Composable
fun PermissionsUI(navController: NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Permissions") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Permissions",
                    fontSize = 30.sp,
                    fontFamily = Constants.DONGLE_BOLD
                )
            }
        }
    )
}

@Composable
fun BlockedUsers(navController: NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Blocked Users") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Blocked Users",
                    fontSize = 30.sp,
                    fontFamily = Constants.DONGLE_BOLD
                )
            }
        }
    )
}

@Composable
fun SavedLoginInfo(navController: NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Saved Login Info") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Saved Login Info",
                    fontSize = 30.sp,
                    fontFamily = Constants.DONGLE_BOLD
                )
            }
        }
    )
}

@Composable
fun MyData(navController: NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "My Data") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "My Data",
                    fontSize = 30.sp,
                    fontFamily = Constants.DONGLE_BOLD
                )
            }
        }
    )
}


@Composable
fun Logout(navController: NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Logout") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Logout",
                    fontSize = 30.sp,
                    fontFamily = Constants.DONGLE_BOLD
                )
            }
        }
    )
}