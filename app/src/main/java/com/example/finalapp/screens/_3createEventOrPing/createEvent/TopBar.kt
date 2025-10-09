package com.example.finalapp.screens._3createEventOrPing.createEvent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventTopBar2(isActive:Boolean,onBackClicked:()->Unit, onNext: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Create Event", fontFamily = Constants.FONT_MEDIUM, color = Constants.HOME_TOP_BAR_COLOR, fontSize = 18.sp) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Constants.HOME_TOP_BAR_ICON_COLOR),
        actions = {
            Card(
                modifier = Modifier.padding(end = 16.dp)
                    .wrapContentSize()
                    .clickable {
                        if (isActive) {
                            onNext()
                        }
                    },
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(containerColor = if(isActive) Color(0xFF081670) else Color.LightGray)
            ) {
                Text(
                    text = "Upload",
                    fontFamily = Constants.FONT_LIGHT,
                    color = if(isActive ) Color.White else Color.DarkGray,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 16.sp
                )
            }
            },
        navigationIcon = {
            Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription ="", modifier = Modifier.size(24.dp).clickable { onBackClicked() }, colorFilter = ColorFilter.tint(Color.Black))
        },
        modifier = Modifier.shadow(elevation = 10.dp)
    )
}

