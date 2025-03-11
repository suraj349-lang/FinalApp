package com.example.finalapp.screens._5settings

import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.common.BackImage
import com.example.finalapp.ui.theme.LIGHT_GREEN
import com.example.finalapp.ui.theme.LIGHT_GREY_BG_COLOR
import com.example.finalapp.ui.theme.homeTopBarIconsColor
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.viewmodels.AuthViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreenUI(navController: NavHostController,authViewModel: AuthViewModel){
    val buttonVisible= remember { mutableStateOf(false) };
    val list= listOf(
        ProfileObject.profile?.let { MyAccount("Name", it.name,SCREENS.EDIT_NAME.route) },
        ProfileObject.profile?.let { MyAccount("Username", it.username,SCREENS.EDIT_USER_NAME.route) },
        ProfileObject.profile?.let { MyAccount("Phone Number", it.number,SCREENS.PHONE_NUMBER.route) },
        MyAccount("Password","",SCREENS.PASSWORD.route) ,
        MyAccount("Delete Account","",SCREENS.DELETE_ACCOUNT.route)
    )
    val listSupportAndFeedback= listOf(SupportAndFeedBack("Bugs and Suggestions",SCREENS.BUGS_AND_SUGGESTION.route),SupportAndFeedBack("Safety and Privacy",SCREENS.SAFETY_AND_PRIVACY.route), SupportAndFeedBack("Help Centre",SCREENS.HELP_CENTRE.route))

    val moreInformation= listOf(MoreInformation("Privacy Policy",SCREENS.PRIVACY_POLICY.route),MoreInformation("Safety Centre",SCREENS.SAFETY_CENTRE.route),MoreInformation("Terms of Service",SCREENS.TERMS_OF_SERVICE.route),MoreInformation("Other legal",SCREENS.OTHER_LEGAL.route))
    val accountActions= listOf(AccountAction("Clear Search History",SCREENS.CLEAR_SEARCH_HISTORY.route),AccountAction("Permissions",SCREENS.PERMISSIONS.route),AccountAction("Blocked Users",SCREENS.BLOCKED_USERS.route),AccountAction("Saved Login Info",SCREENS.SAVED_LOGIN_INFO.route),AccountAction("My Data",SCREENS.MY_DATA.route),AccountAction("Log Out",SCREENS.LOG_OUT.route))

    Scaffold(
        topBar = { SettingsTopBar { navController.navigate(SCREENS.HOME.route) } },
        bottomBar = { BottomBar(navController =navController , state = buttonVisible,modifier = Modifier.height(45.dp)) }
    ) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                MyAccount(list as List<MyAccount>,navController)
                SupportAndFeedback(list = listSupportAndFeedback,navController)
                MoreInformation(list = moreInformation,navController)
                AccountActions(list = accountActions,navController)
            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(onBackClicked:()->Unit) {
    TopAppBar(title = {
        Text(
            "Settings",
            fontSize = 20.sp,
            fontWeight= FontWeight.SemiBold,
            modifier = Modifier, color = homeTopBarIconsColor, fontFamily = Constants.FONT_MEDIUM
        )
    },
    navigationIcon = { BackImage(onBackClicked)})
}

@Composable
fun MyAccount(list: List<MyAccount>,navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = LIGHT_GREY_BG_COLOR),
            border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
        ) {
            Text(
                text = "MY ACCOUNT",
                color = LIGHT_GREEN,
                fontFamily = DONGLE_BOLD,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        list.forEach { 
            MyAccountUI(item = it, navController)
        }
    }
}

@Composable
fun MyAccountUI(item:MyAccount,navController: NavHostController) {
    Card(
        modifier = Modifier.clickable { navController.navigate(item.route) }
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
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.key, fontFamily = DONGLE_BOLD, fontSize = 18.sp)
            Text(text = item.value, fontFamily = DONGLE_BOLD, fontSize = 22.sp)
        }
    }
    
}

@Composable
fun SupportAndFeedback(list: List<SupportAndFeedBack>,navController: NavHostController) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = LIGHT_GREY_BG_COLOR),
            border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Text(text = "SUPPORT AND FEEDBACK", color = Color(0xFF5BBB07), fontFamily = DONGLE_BOLD, fontSize = 20.sp)
            }

        }
        list.forEach { item ->
            Card(
                modifier = Modifier.clickable { navController.navigate(item.route) }
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
                    Text(text = item.key, fontFamily = DONGLE_BOLD, fontSize = 18.sp)
                }
            }
        }
    }
}
@Composable
fun MoreInformation(list: List<MoreInformation>, navController: NavHostController) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = LIGHT_GREY_BG_COLOR),
            border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Text(text = "MORE INFORMATION", color = Color(0xFF5BBB07), fontFamily = DONGLE_BOLD, fontSize = 20.sp)
            }

        }
        list.forEach { item ->
            Card(
                modifier = Modifier.clickable { navController.navigate(item.route) }
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = LIGHT_GREY_BG_COLOR),
            border = BorderStroke(width = 0.25.dp, color = Color.LightGray)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Text(text = "ACCOUNT ACTIONS", color = Color(0xFF5BBB07), fontFamily = DONGLE_BOLD, fontSize = 20.sp)
            }

        }
        list.forEach { item ->
            Card(
                modifier = Modifier.clickable { navController.navigate(item.route) }
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
                    Text(text = item.key, fontFamily = DONGLE_BOLD, fontSize = 18.sp)
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