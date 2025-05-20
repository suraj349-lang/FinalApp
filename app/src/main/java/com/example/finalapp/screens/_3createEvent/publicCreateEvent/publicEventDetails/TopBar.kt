package com.example.finalapp.screens._3createEvent.publicCreateEvent.publicEventDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants

@Composable
fun PublicEventDetailsTopBar(eventTitle:String) {
    TopAppBar(
        modifier = Modifier.shadow(40.dp),
        title = { Text(text = eventTitle, fontFamily = Constants.FONT_MEDIUM)},
        navigationIcon = { Image(painter = painterResource(id = R.drawable.back), contentDescription ="", modifier = Modifier.size(40.dp) )},
        actions ={}
    )
}