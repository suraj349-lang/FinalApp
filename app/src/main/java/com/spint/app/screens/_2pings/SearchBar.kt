package com.spint.app.screens._2pings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.spint.app.R
import com.spint.app.utils.constants.Constants


@Composable
fun SearchBar() {
    var searchText by remember {
        mutableStateOf("")
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            shape = RoundedCornerShape(30.dp),
            maxLines=1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = Color.Red,
                backgroundColor = Color.Black
            ),
            placeholder = { Text(text = "Search", fontFamily = Constants.FONT_MEDIUM, color = Color.White) },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.search_new),
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "", modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}
