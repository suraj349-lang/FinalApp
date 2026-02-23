package com.spint.app.screens.duel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.R
import com.spint.app.utils.constants.Constants

@Composable
fun DuelTopBar(modifier: Modifier = Modifier,onBackClicked:()-> Unit) {
    TopAppBar(
        title = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Column(
                    Modifier
                        .fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    Text("Duel", fontSize = 20.sp, color = Color.White, fontFamily = Constants.FONT_MEDIUM, lineHeight = 12.sp)
                    Text("connect on interest...", fontSize = 9.sp, color = Color.White, fontFamily = Constants.ROBOTO_CONDENSED, lineHeight = 12.sp)
                }
            }
        },
        backgroundColor = Constants.HOME_TOP_BAR_COLOR,
        // modifier = Modifier.shadow(elevation = 20.dp, spotColor =Color.White),
        navigationIcon = {
            Image(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "", colorFilter = ColorFilter.tint(Color.White), modifier = Modifier.size(24.dp).clickable{onBackClicked()})
            // Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription ="", modifier = Modifier.size(24.dp) , colorFilter = ColorFilter.tint(Color.White) )
        },
        modifier = Modifier.shadow(elevation = 10.dp, spotColor = Color.White).statusBarsPadding(),
        elevation = AppBarDefaults.TopAppBarElevation
    )
}

