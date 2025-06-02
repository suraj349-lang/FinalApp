package com.example.finalapp.screens._2pings

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants


@Composable
fun SearchBar() {
    var searchText by remember {
        mutableStateOf("")
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFE))
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            shape = RoundedCornerShape(30.dp),
            maxLines=1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 4.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = Color.Red
            ),
            placeholder = { Text(text = "Search", fontFamily = Constants.FONT_MEDIUM) },
            trailingIcon = { Image(painter = painterResource(id = R.drawable.search_new), contentDescription = "", modifier = Modifier.size(20.dp))}
        )
    }
}
