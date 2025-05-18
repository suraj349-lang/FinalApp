package com.example.finalapp.screens._3createEvent.privateCreateEvent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventTopBar2(showButton:Boolean,onButtonClicked:()->Unit) {
    TopAppBar(title = { Text(text = "Create Event") },
        actions = {
            if(showButton) {
                Button(onClick = { onButtonClicked()}) {
                    Text(text = "Create Event")
                }
            } },
        navigationIcon = {
            Image(painter = painterResource(id = R.drawable.back), contentDescription ="", modifier = Modifier.size(30.dp) ) }
    )
}
