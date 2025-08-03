package com.example.finalapp.screens._3createEventOrPing.createPing

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
import com.example.finalapp.utils.UserObject
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.finalapp.model.pings.PingRequestDto
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.uriToFile
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.UserLocationObject
import com.example.finalapp.viewmodels.EventsViewModel
import java.io.File




enum class CREATE_PING {
    IMAGE,TYPE,CAPTION,LOCATION,PREVIEW
}
@Composable
fun CreatePingMainScreen(navController: NavController, eventsViewModel: EventsViewModel) {
    var page by remember {
        mutableStateOf(CREATE_PING.IMAGE)
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
    val userLocation by UserLocationObject.userLocation.collectAsState()

    val context= LocalContext.current
    var showButton by remember {
        mutableStateOf(false)
    }
    val createPing by eventsViewModel.createPingResponse.collectAsState()
    when(createPing){
        is RequestState.Idle ->{}
        is RequestState.Error ->{
            Toast.makeText(LocalContext.current,"Error creating ping",Toast.LENGTH_SHORT).show()
            navController.navigate(SCREENS.HOME.route){
                popUpTo(0)
            }
        }
        is RequestState.Success->{

            Toast.makeText(LocalContext.current,"Ping created successfully",Toast.LENGTH_SHORT).show()
            navController.navigate(SCREENS.HOME.route){
                popUpTo(0)
            }
        }
        is RequestState.Loading->{
            CircularProgressIndicator()
        }
    }



    Scaffold(
        topBar = {
            CreatePingTopBar(showButton){
                val uri = imageUri
                var imageFile by mutableStateOf<File?>(null)
                if(uri != Uri.EMPTY) imageFile = uriToFile(uri!!, context )
                imageFile?.let {
                    eventsViewModel.uploadImageAndThenCreateEvent(UserObject.user.value.user , it){ imageKey->
                        eventsViewModel.createPing(
                            PingRequestDto(
                                user =UserObject.user.value.user ,
                                userName = UserObject.user.value.userName,
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
                CREATE_PING.IMAGE ->{
                    AddImageCreatePing({imageUri=it }){
                         page= CREATE_PING.TYPE
                    }
                }
                CREATE_PING.TYPE ->{
                    AddPingTypeCreatePing(type ,{type=it }){
                        page= CREATE_PING.CAPTION
                    }

                }
                CREATE_PING.CAPTION ->{
                    AddCaptionCreatePing(caption,{caption=it }){
                        page= CREATE_PING.LOCATION
                    }
                }
                CREATE_PING.LOCATION ->{
                    AddLocationCreatePing(
                        city = userLocation.city ?: "",
                        country = userLocation.country ?: ""
                    ){
                        location=it;
                        page= CREATE_PING.PREVIEW
                    }
                }
                CREATE_PING.PREVIEW ->{
                    showButton=true
                    PreviewCreatePing(uri = imageUri,caption, pingType =type)
                }
            }
        }
        
    }
    
}





