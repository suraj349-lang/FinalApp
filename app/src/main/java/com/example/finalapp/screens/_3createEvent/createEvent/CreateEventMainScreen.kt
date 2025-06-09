package com.example.finalapp.screens._3createEvent.createEvent

import android.net.Uri
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
import com.example.finalapp.utils.ProfileObject
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.finalapp.model.EventRequestDTO
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.uriToFile
import com.example.finalapp.viewmodels.EventsViewModel
import java.io.File




enum class CREATE_EVENT {
    IMAGE,TYPE,CAPTION,LOCATION,PREVIEW
}
@Composable
fun CreateEventMainScreen(navController: NavController, eventsViewModel: EventsViewModel) {
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
    val success by eventsViewModel.createEventIsSuccess.collectAsState()
    val loading by eventsViewModel.createEventIsLoading.collectAsState()
    val context= LocalContext.current
    var showButton by remember {
        mutableStateOf(false)
    }
    if(loading) CircularProgressIndicator()
    if(success){
        Toast.makeText(LocalContext.current,"Event created successfully",Toast.LENGTH_SHORT).show()
        navController.navigate(SCREENS.HOME.route){
            popUpTo(0)
        }
        eventsViewModel.createEventIsSuccess.value=false
    }


    Scaffold(
        topBar = {
            CreateEventTopBar2(showButton){
                val uri = imageUri
                var imageFile by mutableStateOf<File?>(null)
                if(uri != Uri.EMPTY) imageFile = uriToFile(uri!!, context )
                imageFile?.let {
                    eventsViewModel.uploadImageAndThenCreateEvent(ProfileObject.profile?.userId!!, it){imageKey->
                        eventsViewModel.createEvent(
                            EventRequestDTO(
                                user = ProfileObject.profile?.userId!!,
                                userName = ProfileObject.profile?.username!!,
                                title=type,
                                image = imageKey,
                                category = type,
                                location = location,
                                description =caption,
                                expirationTime = "12")
                        )
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
                    AddLocationCreateEvent{
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





