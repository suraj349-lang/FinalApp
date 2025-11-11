package com.spint.app.screens._5settings

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.spint.app.R
import com.spint.app.enums.BugsAndSuggestionsEnum
import com.spint.app.utils.constants.Constants

data class FeatureList(
    val image:Int,
    val title: BugsAndSuggestionsEnum
)
val featuresList = listOf(
    FeatureList(R.drawable.cameranew, BugsAndSuggestionsEnum.CAMERA),
    FeatureList(R.drawable.ping, BugsAndSuggestionsEnum.PINGS),
    FeatureList(R.drawable.event, BugsAndSuggestionsEnum.EVENT),
    FeatureList(R.drawable.drop_profile_new, BugsAndSuggestionsEnum.DROP_PROFILE),
    FeatureList(R.drawable.nearby_chat, BugsAndSuggestionsEnum.DIRECT_CHAT),
    FeatureList(R.drawable.create_event_new, BugsAndSuggestionsEnum.CREATE_EVENT),
    FeatureList(R.drawable.chat_new, BugsAndSuggestionsEnum.CHATS),
    FeatureList(R.drawable.profile_new, BugsAndSuggestionsEnum.PROFILE),
    FeatureList(R.drawable.search_new, BugsAndSuggestionsEnum.SEARCH),
    FeatureList(R.drawable.settings_new, BugsAndSuggestionsEnum.SETTINGS)
)


@Composable
fun FeaturesListScreen(onItemClicked:()->Unit,onBackClicked:()->Unit) {

    val context= LocalContext.current
    val window = (context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.DarkGray.toArgb()
    window.navigationBarColor = Color.DarkGray.toArgb()
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { CommonTopBar(title = "Bug", backGroundColor = Color.LightGray, textColor = Color(0xFF121212)) {
            onBackClicked()
        }})
    {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it), color = Color.LightGray.copy(alpha = 0.7f)) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Where in ${Constants.APP_NAME.capitalize()} did you encounter this issue?",
                    fontSize = 12.sp,
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                featuresList.forEach {screen->
                    FeatureListItem(screen.image, screen.title.displayName){
                        onItemClicked()
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
    }
}

@Composable
fun FeatureListItem(image:Int,title:String,onClick:()->Unit) {
    Card(
        Modifier.clickable { onClick() }
            .fillMaxWidth()
            .height(60.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    modifier = Modifier.size(28.dp),
                    colorFilter = ColorFilter.tint(Color.DarkGray)
                )
                Text(text = title, fontFamily = Constants.FONT_LIGHT, fontSize = 16.sp,color = Color(0xFF161616))
            }
            Image(
                painter = painterResource(id = R.drawable.next),
                contentDescription = "",
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )

        }
    }
}