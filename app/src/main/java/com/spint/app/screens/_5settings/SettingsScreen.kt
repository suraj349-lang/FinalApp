package com.spint.app.screens._5settings

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._3createEventOrPing.CreateEventOrPingBottomSheet
import com.spint.app.screens.common.BackImage
import com.spint.app.ui.theme.LIGHT_GREEN
import com.spint.app.ui.theme.LIGHT_GREY_BG_COLOR
import com.spint.app.utils.UserObject
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.constants.Constants.DONGLE_BOLD
import com.spint.app.viewmodels.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreenUI(navController: NavHostController,authViewModel: AuthViewModel){
    val buttonVisible= remember { mutableStateOf(true) };
    val user by UserObject.user.collectAsState()
    var showSheet by remember {
        mutableStateOf(false)
    }

    val context= LocalContext.current
    val window = (context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.DarkGray.toArgb()
    window.navigationBarColor = Color.DarkGray.toArgb()
    val list= listOf(
         MyAccount("Name", user.name,SCREENS.EDIT_NAME.route) ,
         MyAccount("Username", user.userName,SCREENS.EDIT_USER_NAME.route) ,
         MyAccount("Phone Number", user.number,SCREENS.PHONE_NUMBER.route) ,
         MyAccount("Password","",SCREENS.PASSWORD.route) ,
        // TODO later enable it
        // MyAccount("Delete Account","",SCREENS.DELETE_ACCOUNT.route)
    )
    val listSupportAndFeedback = listOf(
        SupportAndFeedBack("Bugs and Suggestions", SCREENS.BUGS_AND_SUGGESTION.route),
        SupportAndFeedBack("Safety and Privacy", SCREENS.SAFETY_AND_PRIVACY.route),
       // SupportAndFeedBack("Help Centre", SCREENS.HELP_CENTRE.route)
    )

    val moreInformation = listOf(
        MoreInformation("Privacy Policy", SCREENS.PRIVACY_POLICY.route),
        MoreInformation("Safety Centre", SCREENS.SAFETY_CENTRE.route),
        MoreInformation("Terms of Service", SCREENS.TERMS_OF_SERVICE.route),
        MoreInformation("Other legal", SCREENS.OTHER_LEGAL.route)
    )
    val accountActions = listOf(
        AccountAction("Clear Search History", SCREENS.CLEAR_SEARCH_HISTORY.route),
        AccountAction("Permissions", SCREENS.PERMISSIONS.route),
        AccountAction("Blocked Users", SCREENS.BLOCKED_USERS.route),
        AccountAction("Saved Login Info", SCREENS.SAVED_LOGIN_INFO.route),
        AccountAction(
            "My Data",
            SCREENS.MY_DATA.route
        )/*,AccountAction("Log Out",SCREENS.LOG_OUT.route)*/
    )

    Scaffold(
        topBar = { SettingsTopBar { navController.navigate(SCREENS.HOME.route) } },
        bottomBar = {
//            BottomBar(
//                navController = navController,
//                state = buttonVisible,
//                modifier = Modifier.height(45.dp),
//                highlightedTextColor = Constants.BOTTOM_BAR_ACTIVE_TEXT_COLOR,
//                inactiveTextColor = Constants.BOTTOM_BAR_INACTIVE_TEXT_COLOR,
//                inactiveIconColor = Constants.BOTTOM_BAR_INACTIVE_ICON_COLOR,
//                onCreateEventClick = { showSheet = true })
        }
    ) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            Column(modifier = Modifier.background(Color.White.copy(alpha = 0.7f))
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())) {
                MyAccount(list,navController)
                SupportAndFeedback(list = listSupportAndFeedback,navController)
               // AccountActions(list = accountActions,navController)
            }

        }

    }
    CreateEventOrPingBottomSheet(
        showSheet = showSheet,
        onDismiss = {showSheet=false },
        navHostController = navController
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(onBackClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier,
                color = Constants.HOME_TOP_BAR_COLOR,
                fontFamily = Constants.FONT_MEDIUM
            )
        },
        colors=TopAppBarDefaults.mediumTopAppBarColors(containerColor = Constants.HOME_TOP_BAR_ICON_COLOR),
        modifier = Modifier
            .shadow(elevation = 10.dp)
            .statusBarsPadding(),
        navigationIcon = { BackImage(onBackClicked) })
}

@Composable
fun MyAccount(list: List<MyAccount>,navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SettingsTitleCommonTextUI("MY ACCOUNT")
        list.forEach { 
            MyAccountUI(item = it, navController)
        }
    }
}

@Composable
fun SettingsTitleCommonTextUI(title:String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = LIGHT_GREY_BG_COLOR),
        border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
            Text(
                text = title,
                color = LIGHT_GREEN,
                fontFamily = Constants.FONT_LIGHT,
                fontSize = 14.sp
            )
        }
    }
    
}

@Composable
fun MyAccountUI(item:MyAccount,navController: NavHostController) {
    Card(
        modifier = Modifier
            .clickable { navController.navigate(item.route) }
            .fillMaxWidth()
            .height(45.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.key, fontFamily = Constants.FONT_LIGHT, fontSize = 13.sp, color = Color(0xFF121212), fontWeight = FontWeight.Bold)
            Text(text = item.value, fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp, color = Color.DarkGray)
        }
    }
    
}

@Composable
fun SupportAndFeedback(list: List<SupportAndFeedBack>,navController: NavHostController) {

    Column(modifier = Modifier.fillMaxWidth()) {
        SettingsTitleCommonTextUI(title = "SUPPORT AND FEEDBACK")
        list.forEach { item ->
            Card(
                modifier = Modifier
                    .clickable { navController.navigate(item.route) }
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.key, fontFamily = Constants.FONT_LIGHT, fontSize = 13.sp, color = Color(0xFF121212), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
@Composable
fun MoreInformation(list: List<MoreInformation>, navController: NavHostController) {

    Column(modifier = Modifier.fillMaxWidth()) {
        SettingsTitleCommonTextUI(title = "MORE INFORMATION")
        list.forEach { item ->
            Card(
                modifier = Modifier
                    .clickable { navController.navigate(item.route) }
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .clickable { navController.navigate(SCREENS.PRIVACY_POLICY.route) }
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.key, fontFamily = DONGLE_BOLD, fontSize = 18.sp)
                }
            }
        }
    }
}



@Composable
fun AccountActions(list: List<AccountAction>, navController: NavHostController) {

    Column(modifier = Modifier.fillMaxWidth()) {
        SettingsTitleCommonTextUI(title = "ACCOUNT ACTIONS")
        list.forEach { item ->
            Card(
                modifier = Modifier
                    .clickable { navController.navigate(item.route) }
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.key, fontFamily = Constants.FONT_LIGHT, fontSize = 13.sp, color = Color(0xFF121212), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

data class MyAccount(
    val key:String,
    val value:String,
    val route:String
)

data class SupportAndFeedBack(
    val key:String,
    val route: String
)
data class MoreInformation(
    val key:String,
    val route: String
)
data class AccountAction(
    val key:String,
    val route: String
)