package com.example.finalapp.screens._5settings

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.screens._5settings.legal.readRawTextFile
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.finalapp.navigation.SCREENS

@Composable
fun BugsAndSuggestion(navController:NavHostController,onSendClicked:(String)->Unit) {

    val context= LocalContext.current
    val window = (context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.DarkGray.toArgb()
    window.navigationBarColor = Color.DarkGray.toArgb()
    Scaffold(topBar = {
        CommonTopBar(title = "Bugs And Suggestions", backGroundColor = Color.LightGray, textColor = Color(0xFF121212)) {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    BugAndSuggestionItem(
                        title = "Report a bug",
                        subtitle = "Tell us if something is not working as expected",
                        title2 = "Make a suggestion",
                        subtitle2 = "How can we make spint even better?",
                        onReportBugClicked = {navController.navigate(SCREENS.FEATURES_SCREEN.route)},
                        onMakeSuggestionsClicked = {}
                    )
                    //TODO later enable them with snapchat
//                    Spacer(modifier = Modifier.height(30.dp))
//                    Text(
//                        text = "Feedback and Suggestions",
//                        fontFamily = Constants.FONT_MEDIUM,
//                        fontWeight = FontWeight.Bold,
//                        fontSize=20.sp,
//                        color= Color(0xFF121212),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    )
//                    BugAndSuggestionItem(
//                        title = "Report a bug",
//                        subtitle = "Tell us if something is not working as expected",
//                        title2 = "Make a suggestion",
//                        subtitle2 = "How can we make spint even better?"
//                    )

                }
            }
        }
    )

}


@Composable
fun BugAndSuggestionItem(
    title:String="Report a bug",
    subtitle:String="Tell us if something is not working as expected",
    title2:String="Make a suggestion",
    subtitle2:String="How can we make spint even better?",
    onReportBugClicked:()->Unit,
    onMakeSuggestionsClicked:()->Unit,
)
{
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            BugsAndSuggestionsSubItem(title,subtitle){
                onReportBugClicked()
            }
            Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
            BugsAndSuggestionsSubItem(title2,subtitle2){
                onMakeSuggestionsClicked()
            }
        }
    }
}

@Composable
fun BugsAndSuggestionsSubItem(title: String,subtitle: String,onClick:()->Unit) {
    Row(modifier = Modifier.clickable { onClick() }
        .padding(horizontal = 8.dp, vertical = 16.dp)
        .fillMaxWidth()
        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight()
        ) {
            Text(text = title, fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp,color = Color(0xFF121212))
            Text(text = subtitle, fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp, color = Color.Gray)
        }
        Image(painter = painterResource(id = R.drawable.next), contentDescription = "", modifier = Modifier.size(16.dp), colorFilter = ColorFilter.tint(Color.DarkGray))
    }
}


@Composable
fun SafetyAndPrivacy(navController:NavHostController) {
    val context= LocalContext.current
    val text by remember { mutableStateOf( readRawTextFile(context, R.raw.safetyandprivacy)) }
    Scaffold(topBar = {
        CommonTopBar(title = "Safety And Privacy") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                item {
                    Text(
                        text = text,
                      //  style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    )

}

@Composable
fun HelpCentre(navController:NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Help Centre") {
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
                Text(text = "Help Centre", fontSize = 30.sp, fontFamily = Constants.DONGLE_BOLD)
            }
        }
    )

}
