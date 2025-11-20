package com.spint.app.screens._4profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.AppIcons
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.constants.Constants.DONGLE_BOLD

enum class SheetValue { Collapsed, PartiallyExpanded, Expanded }
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ImageUpdateBottomSheet(showSheet: Boolean,
                           onDismiss: () -> Unit,
                           currentProfileImage:String?,
                           navHostController: NavHostController,
                           onChangeImageClicked: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    if (showSheet){
        ModalBottomSheet(onDismissRequest = { onDismiss()}, sheetState = sheetState, tonalElevation = 20.dp, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Column(modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)) {
                    GlideImage(model = imagePrefix+currentProfileImage, contentDescription ="", modifier = Modifier.clip(shape = RoundedCornerShape(12.dp)).fillMaxSize(), contentScale = ContentScale.Crop )
                }
//                Text(
//                    "Update Image",
//                    fontSize = 20.sp,
//                    fontFamily = Constants.FONT_MEDIUM,
//                    color = Color.LightGray
//                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 20.dp)) {
                    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = AppIcons.PERSON),
                            contentDescription = ""
                            , modifier = Modifier.size(50.dp)
                        )
                        Text(text = "Remove", fontFamily = Constants.FONT_LIGHT,color = Color.LightGray, fontSize = 12.sp)

                    }
                    Column(modifier = Modifier.wrapContentSize().clickable { onChangeImageClicked() }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = AppIcons.GALLERY_ICON),
                            contentDescription = ""
                            , modifier = Modifier.size(50.dp)
                        )
                        Text(text = "Update",  fontFamily = Constants.FONT_LIGHT,color = Color.LightGray, fontSize = 12.sp)

                    }
//                    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                        Image(
//                            painter = painterResource(id = AppIcons.CAMERA_ICON),
//                            contentDescription = ""
//                            , modifier = Modifier.size(50.dp)
//                        )
//                        Text(text = "Camera", fontFamily = DONGLE_BOLD,color = Color.DarkGray, fontSize = 14.sp)
//
//                    }

                }
            }
        }
    }}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageUpdateDialogBox(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    image: String?,
    onGalleryClicked: () -> Unit,
    onCameraClicked: () -> Unit
) {
    if(showSheet){
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = false)
    ) {
        Box(modifier = Modifier
            .wrapContentSize()
            .clip(shape = RoundedCornerShape(12.dp))
            .border(width = 0.5.dp, shape = RoundedCornerShape(12.dp), color = Color.LightGray)
            .background(color = Color.White) ){
            Column(modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)) {
                    GlideImage(model = image, contentDescription ="", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop )
                }
                Divider(thickness = 1.dp, color = Color.DarkGray)
                Text(
                    "Update Image",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily =DONGLE_BOLD,
                    color = Color.DarkGray
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = AppIcons.PERSON),
                            contentDescription = ""
                            , modifier = Modifier.size(50.dp)
                        )
                        Text(text = "Remove image", fontFamily = DONGLE_BOLD,color = Color.DarkGray, fontSize = 14.sp)

                    }
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .clickable { onGalleryClicked();onDismiss() }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = AppIcons.GALLERY_ICON),
                            contentDescription = ""
                            , modifier = Modifier.size(50.dp)
                        )
                        Text(text = "Gallery", fontFamily = DONGLE_BOLD,color = Color.DarkGray, fontSize = 14.sp)

                    }
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .clickable { onCameraClicked();onDismiss() }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = AppIcons.CAMERA_ICON),
                            contentDescription = ""
                            , modifier = Modifier.size(50.dp)
                        )
                        Text(text = "Camera", fontFamily = DONGLE_BOLD,color = Color.DarkGray, fontSize = 14.sp)

                    }

                }
            }
        }
    }}}