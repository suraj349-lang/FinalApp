package com.example.finalapp.screens.dialogBox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DialogLoading() {
    Dialog(onDismissRequest = {  }) {
        Card(
            modifier = Modifier
                .wrapContentSize()
            , shape = RoundedCornerShape((6.dp)),
        ) {
            Column(modifier = Modifier
                .wrapContentSize()
                .padding(8.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                GlideImage(model  = R.drawable.loading, contentDescription ="Loading", modifier = Modifier.size(40.dp))

            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DialogError(onBackClicked: () -> Unit) {
    Dialog(onDismissRequest = { onBackClicked() }, properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)) {
        Card(
            modifier = Modifier
                .wrapContentSize()
            , shape = RoundedCornerShape((6.dp)),
        ) {
            Column(modifier = Modifier
                .wrapContentSize()
                .padding(8.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                GlideImage(model  = R.drawable.error, contentDescription ="Error", modifier = Modifier.size(40.dp))
                Text(text = "Error connecting to server")

            }

        }
    }
}