package com.example.finalapp.screens._1home.eventDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DirectChat
import com.example.finalapp.model.User
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants

@Composable
fun EventNearBy() {
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn{
            items(listOf<User>()) { user ->
              EventNearByItem(user)
            }
        }
    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EventNearByItem(
    item:User,
    onProfileClicked: () -> Unit = {},
    onSendMessageClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .clickable {
                    onProfileClicked()
                }
                .padding(horizontal = 16.dp)
                .wrapContentWidth()
                .height(300.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            GlideImage(
                model = imagePrefix+item.profileImage,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name ,
                fontSize = 20.sp,
                fontFamily = Constants.USER_NAME_FONT, fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { onSendMessageClicked() },
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f) //.wrapContentHeight().fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            )
        ) {
            Row(modifier = Modifier.wrapContentWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(painter = painterResource(id = R.drawable.chat_new), contentDescription ="", modifier = Modifier.size(16.dp), colorFilter = ColorFilter.tint(
                    Color.White) )
                Text(text = "Send Message", color = Color.White,fontFamily = Constants.FONT_LIGHT)
            }

        }
        Divider(color = floatingActionBtnColor, thickness = 0.5.dp)

    }
}
