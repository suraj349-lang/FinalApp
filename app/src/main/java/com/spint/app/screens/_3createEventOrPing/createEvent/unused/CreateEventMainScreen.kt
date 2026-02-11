package com.spint.app.screens._3createEventOrPing.createEvent.unused

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.spint.app.utils.UserObject
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.spint.app.model.Event
import com.spint.app.screens._3createEventOrPing.createEvent.AddCaptionCreateEvent
import com.spint.app.screens._3createEventOrPing.createEvent.AddEventTypeCreateEvent
import com.spint.app.screens._3createEventOrPing.createEvent.AddImageCreateEvent
import com.spint.app.screens._3createEventOrPing.createEvent.AddLocationCreateEvent
import com.spint.app.screens._3createEventOrPing.createEvent.CreateEventTopBar2
import com.spint.app.screens._3createEventOrPing.createEvent.PreviewCreateEvent
import com.spint.app.screens.dialogBox.uriToFile
import com.spint.app.utils.UserLocationObject
import com.spint.app.viewmodels.HomeViewModel
import java.io.File




enum class CREATE_EVENT {
    IMAGE,TYPE,CAPTION,LOCATION,PREVIEW
}
@Composable
fun CreateEventMainScreenOld(parentEventId:String ?= null, navController: NavController, homeViewModel: HomeViewModel) {
    var page by remember {
        mutableStateOf(CREATE_EVENT.IMAGE)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }
    var caption by remember {
        mutableStateOf("")
    }
    var type by remember {
        mutableStateOf("")
    }
    var location by remember {
        mutableStateOf("")
    }

    val context= LocalContext.current
    val userLocation by UserLocationObject.userLocation.collectAsState()
    var showButton by remember {
        mutableStateOf(false)
    }



    Scaffold(
        topBar = {
            CreateEventTopBar2(
                isActive = showButton,
                onBackClicked = {navController.navigateUp()}
            ){
                val uri = imageUri
                var imageFile by mutableStateOf<File?>(null)
                if(uri != Uri.EMPTY) imageFile = uriToFile(uri!!, context )
                imageFile?.let {
                    homeViewModel.uploadImageAndThenCreateEvent(UserObject.user.value.user , it){ imageKey->
                        homeViewModel.createEvent(
                            Event(
                                user = UserObject.user.value.user ,
                                userName = UserObject.user.value.userName,
                                title=type,
                                image = imageKey,
                                location = location,
                                description =caption,
                                parentPostId = if (!parentEventId.isNullOrEmpty()) parentEventId else null,
                                isChildPost = !parentEventId.isNullOrEmpty(),
                                expirationTime = "12")

                        )
                        Log.i("Create event", "CreateEventMainScreenOld:$ ")
                    }
                }
            }
        }
        , modifier = Modifier.fillMaxSize()) { paddingValues ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when(page){
                CREATE_EVENT.IMAGE ->{
                    AddImageCreateEvent({imageUri=it }){
                         page= CREATE_EVENT.TYPE
                    }
                }
                CREATE_EVENT.TYPE ->{
                    AddEventTypeCreateEvent(type ,{type=it }){
                        page= CREATE_EVENT.CAPTION
                    }

                }
                CREATE_EVENT.CAPTION ->{
                    AddCaptionCreateEvent(caption,{caption=it }){
                        page= CREATE_EVENT.LOCATION
                    }
                }
                CREATE_EVENT.LOCATION ->{
                    AddLocationCreateEvent(
                        city = userLocation.city ?: "",
                        country = userLocation.country ?: ""
                    ){
                        location=it;
                        page= CREATE_EVENT.PREVIEW
                    }
                }
                CREATE_EVENT.PREVIEW ->{
                    showButton=true
                    PreviewCreateEvent(uri = imageUri,caption, eventType =type)
                }
            }
        }
        
    }
    
}





