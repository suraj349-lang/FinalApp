package com.example.finalapp.screens._1home._1_1Events


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.constants.Constants.DONGLE_LIGHT

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CommentBottomSheet(
//    showSheet: Boolean,
//    onDismiss: () -> Unit
//) {
//    if (showSheet) {
//        ModalBottomSheet(
//            onDismissRequest = { onDismiss() },
//            modifier = Modifier
//                .fillMaxHeight(0.9f) // 90% of screen height
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//            ) {
//                // Scrollable Comment List
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(bottom = 80.dp) // Leaves space for input field
//                        .padding(horizontal = 16.dp)
//                ) {
//                    items(20) {
//                        Text(
//                            text = "Testing comment",
//                            fontFamily = DONGLE_BOLD,
//                            fontSize = 20.sp,
//                            modifier = Modifier.padding(16.dp)
//                        )
//                    }
//                }
//
//                // Fixed Input Row at Bottom
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .align(Alignment.BottomCenter) // Sticks to bottom
//                        .background(Color.White)
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                        .imePadding() // Pushes up when keyboard appears
//                        .navigationBarsPadding(), // Keeps above system buttons
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    OutlinedTextField(
//                        value = "",
//                        onValueChange = {},
//                        modifier = Modifier.weight(1f)
//                    )
//                    Button(
//                        onClick = { /* TODO: Handle comment */ },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
//                    ) {
//                        Text(text = "Comment", fontSize = 20.sp, fontFamily = DONGLE_BOLD, color = Color.White)
//                    }
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CommentBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit
) {
    val sheetState= rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var comment by remember{ mutableStateOf("") }
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState=sheetState,
            modifier = Modifier.heightIn(min=200.dp,max=700.dp) // Open at 90% of screen
        ) {
            Column(
                modifier = Modifier
                    .heightIn(min = 200.dp, max = 700.dp)
                    .navigationBarsPadding()
            ) {
                PostCommentData()
                LazyColumn(
                    modifier = Modifier
                        .weight(0.9f) // Allows scrolling
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 700.dp)
                        .padding(bottom = 8.dp)
                ) {
                    items(20) {
                       CommentItem()
                    }
                }
                
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlideImage(model = ProfileObject.profile?.profileImage, contentDescription ="", modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(40.dp), contentScale = ContentScale.Crop )
                        TextField(
                            value =comment,
                            onValueChange = {comment=it},
                            modifier = Modifier.fillMaxWidth(0.7f),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text(text = "Type your comment...", fontFamily = DONGLE_BOLD)}
                        )
                        Button(
                            onClick = {  },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray), shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Post", fontSize = 20.sp, fontFamily = DONGLE_BOLD, color = Color.White)
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CommentItem() {
    Row(modifier = Modifier.padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        GlideImage(
            model = R.drawable.profile_image_1,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(35.dp)
                .clip(shape = CircleShape)
        )
        Text(text = "testingUser__123", fontFamily = DONGLE_BOLD, fontSize = 16.sp)
        Text(text = "This is not good by government", fontFamily = DONGLE_LIGHT, fontSize = 20.sp, modifier = Modifier.padding(start=6.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostCommentData() {
    Column {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        GlideImage(
            model = R.drawable.profile_image_2,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(35.dp)
                .clip(shape = CircleShape)
        )
        Text(text = "singhSuraj__94", fontFamily = DONGLE_BOLD, fontSize = 24.sp)
        Text(text = "Farmers Protest", fontFamily = DONGLE_LIGHT, fontSize = 22.sp, modifier = Modifier.padding(start=6.dp))
    }
        Divider(modifier = Modifier.fillMaxWidth().padding(top = 2.dp), thickness = 0.5.dp, color = Color.DarkGray)
    }
}

