package com.example.finalapp.screens._1home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DirectChat
import com.example.finalapp.model.DirectChatRequest
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.ui.theme.PURPLE
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.UserLocation
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    authViewModel: AuthViewModel,
    eventsViewModel: EventsViewModel,
    navController: NavHostController
) {

    val checked by eventsViewModel.checked
    val shareProfileClicked by eventsViewModel.shareProfileClicked
    val directChatResponseState by eventsViewModel.directChatResponse.collectAsState()
    val nearByUsersList by eventsViewModel.nearByUsersList.collectAsState()
    LaunchedEffect(key1 =shareProfileClicked){
        if(shareProfileClicked ){
            eventsViewModel.checked.value=!checked
            eventsViewModel.sendDirectChatData(DirectChatRequest( "677b4df1842c1c465293fc2f",authViewModel.latitude.value,authViewModel.longitude.value))
        }
        else {
           // eventsViewModel.emptyNearByUsersList()
        }
        eventsViewModel.shareProfileClicked.value = false

    }

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 16.dp, top = 8.dp)
                    ) {
                        SwitchWithIcon(checked) {
                            eventsViewModel.shareProfileClicked.value = true
                        }
                    }
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth()
                            .height(60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = if(checked)"Users near you" else "Share profile nearby" ,
                            fontFamily = DONGLE_BOLD,
                            fontSize = 24.sp
                        )
                    }
                    DirectChatUI(
                        scrollBehavior,
                        eventsViewModel,
                        checked
                    ) { eventsViewModel.shareProfileClicked.value = true }
                }


                }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatUI(
    scrollBehavior: TopAppBarScrollBehavior,
    eventsViewModel: EventsViewModel,
    checked: Boolean,
    onShareProfileClicked: () -> Unit
) {
    if (!checked ) {
        ShareProfileForDirectChat{ onShareProfileClicked() }
    } else {
        DirectChatProfiles(scrollBehavior, eventsViewModel )
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatProfiles(scrollBehavior: TopAppBarScrollBehavior, eventsViewModel: EventsViewModel) {
    val chatState by eventsViewModel.directChatResponse.collectAsState()
    val userList = eventsViewModel.nearByUsersList.collectAsLazyPagingItems()
    val context = LocalContext.current

    when (chatState) {
        is RequestState.Loading -> {
            DialogLoading()
        }
        is RequestState.Error -> {
            Text("Error: ${(chatState as RequestState.Error)}")
        }
        is RequestState.Success -> {
            LazyColumn(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
                items(userList) { item ->
                    item?.let {
                        DirectChatItem(it) {
                            Toast.makeText(
                                context,
                                "Clicked to chat with this person",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DirectChatItem(user: DirectChat?, onDirectChatItemClicked: () -> Unit) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .size(150.dp)
                .padding(top = 4.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            GlideImage(
                model = if(user?.userId?.profileImage?.isNotEmpty() == true) user.userId.profileImage else R.drawable.profile_image_2,
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
                text = if(user?.userId?.name?.isNotEmpty() == true) user!!.userId.name.capitalize() else "...",
                fontSize = 25.sp,
                fontFamily = DONGLE_BOLD
            )
            // Text(text = " ,100m.", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.oreganoregular)))
        }

        Button(
            onClick = { onDirectChatItemClicked() },
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f) //.wrapContentHeight().fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.White
            )
        ) {
            Text(text = "Send Message", color = Color.Black,fontFamily = DONGLE_BOLD)
        }
        Divider(color = Color.LightGray, thickness = 0.5.dp)

    }
}




@Composable
fun SwitchWithIcon(checked: Boolean,onClick:(value:Boolean)->Unit) {
    Switch(
        checked = checked,
        onCheckedChange = {
            onClick(it)
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,// MaterialTheme.colorScheme.primary,
            checkedTrackColor = Color(0xFF053E77),
            uncheckedThumbColor = Color.DarkGray,
            uncheckedTrackColor = Color.LightGray,
        ), modifier = Modifier.padding(0.dp)
    )


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShareProfileForDirectChat(onShareProfileClicked:()->Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(1f)) {
        Image(painter = painterResource(id = R.drawable.whatsapp), contentDescription ="", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize() )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                modifier = Modifier.fillMaxWidth(0.8f).wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F7FA)),
                elevation = CardDefaults.cardElevation(100.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Row(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 10.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//                        Image(painter = painterResource(id = R.drawable.location_new), contentDescription ="", modifier = Modifier.size(30.dp) )
//                        UserLocation.address?.let { DroppedProfileLocation(location = it) }
//                        UserLocation.address?.let { Text(text = it, fontFamily = DONGLE_BOLD, overflow = TextOverflow.Ellipsis, maxLines = 1,fontSize =20.sp, color = Color.Black) }
//                    }
                    UserLocation.address?.let { DroppedProfileLocation(location = it,true) }
                    Card(
                      modifier = Modifier.size(150.dp),
                      shape = CircleShape,
                      border = BorderStroke(width = 1.dp, color = Color.LightGray)) {
                    GlideImage(
                        model = ProfileObject.profile?.profileImage,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
                ProfileObject.profile?.let { Text(text = it.username, overflow = TextOverflow.Ellipsis, fontFamily = DONGLE_BOLD, fontSize =24.sp) }
                Button(
                    onClick = { onShareProfileClicked() },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PURPLE, contentColor = Color.White)) {
                    Text(text = "Share Profile", fontFamily = DONGLE_BOLD, fontSize = 20.sp)
                }
            }
        }
        }

    }

}