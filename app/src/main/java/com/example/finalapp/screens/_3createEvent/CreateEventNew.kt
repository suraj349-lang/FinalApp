package com.example.finalapp.screens._3createEvent

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.utils.ProfileObject
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.finalapp.R
import com.example.finalapp.model.EventRequestDTO
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._4profile.EventTopic
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.viewmodels.EventsViewModel
import java.io.File




enum class CREATE_EVENT {
    IMAGE,TYPE,CAPTION,LOCATION,MISCELLANEOUS
}
@Composable
fun CreateEventNew(navController: NavController, eventsViewModel: EventsViewModel) {
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
    val success by eventsViewModel.createEventIsSuccess.collectAsState()
    val loading by eventsViewModel.createEventIsLoading.collectAsState()
    if(loading) CircularProgressIndicator()
    if(success){
        Toast.makeText(LocalContext.current,"Event created successfully",Toast.LENGTH_SHORT).show()
        navController.navigate(SCREENS.HOME.route){
            popUpTo(0)
        }
    }

    Scaffold(
        topBar = {
            CreateEventTopBar2()
        }
        , modifier = Modifier.fillMaxSize()) { paddingValues ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when(page){
                CREATE_EVENT.IMAGE ->{
                    CreateEventImageScreen({imageUri=it }){
                         page= CREATE_EVENT.TYPE
                    }
                }
                CREATE_EVENT.TYPE ->{
                    CreateEventTypeScreen(type ,{type=it }){
                        page= CREATE_EVENT.CAPTION
                    }

                }
                CREATE_EVENT.CAPTION ->{
                    CreateEventCaption(caption,{caption=it }){
                        page= CREATE_EVENT.LOCATION
                    }
                }
                CREATE_EVENT.LOCATION ->{
                    YourLocation{
                        page= CREATE_EVENT.MISCELLANEOUS
                    }
                }
                CREATE_EVENT.MISCELLANEOUS ->{
                    CreateEventFinalScreen(uri = imageUri,caption, eventType =type){
                        eventsViewModel.createEvent(EventRequestDTO(user = ProfileObject.profile?.userId!!, userName = ProfileObject.profile?.username!!, image = imageUri.toString(), category = type, location = ProfileObject.profile?.address!!, offer =caption, expirationTime = "!2"))
                    }
                }
            }
        }
        
    }
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventTopBar2() {
    TopAppBar(title = { Text(text = "Create Event")},
        actions = {


    }, navigationIcon = {
        Image(painter = painterResource(id = R.drawable.back), contentDescription ="", modifier = Modifier.size(30.dp) )
        })
    
}

@Composable
fun CreateEventImageScreen(onImageUriChange:(Uri?)->Unit,onNextClicked:()->Unit) {
    val context = LocalContext.current

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
            result.uriContent?.let { onImageUriChange(it) }
        }
    }
    var showChooser by remember {
        mutableStateOf(false)
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { cropImageLauncher.launch(CropImageContractOptions(it, CropImageOptions())) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraImageUri.value != null) {
            cropImageLauncher.launch(CropImageContractOptions(cameraImageUri.value, CropImageOptions()))
        }
    }
    if (imageUri != null) {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
    }
    Box(Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap!!.asImageBitmap(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .fillMaxHeight(0.7f)
                            .border(width = 0.5.dp, color = Color.LightGray)
                    )
                    Text(text = "Edit image", fontWeight = FontWeight.Bold, fontFamily = DONGLE_BOLD, fontSize = 16.sp)
                    Button(onClick =  onNextClicked ) {
                        Text(text = "Next")
                        
                    }
                } else {

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp, start = 20.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Add an Image", fontFamily = DONGLE_BOLD, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera_colored),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                "Take a photo",
                                fontSize = 24.sp,
                                fontFamily = DONGLE_BOLD,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            File(context.cacheDir, "temp_image.jpg")
                                        )
                                        cameraImageUri.value = uri
                                        cameraLauncher.launch(uri)
                                        showChooser = false
                                    }
                            )

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.gallery_colored),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )

                            Text(
                                "Select from device",
                                fontSize = 24.sp,
                                fontFamily = DONGLE_BOLD,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        galleryLauncher.launch("image/*")
                                        showChooser = false
                                    }

                            )
                        }
                    }
                }
            }
        }
}


@Composable
fun CreateEventTypeScreen(
    event: String,
    onEventChange: (String) -> Unit,
    onNextClicked: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    // Filter the enum list based on user input
    val suggestions = remember(event) {
        if (event.isBlank()) emptyList()
        else EventTopic.values().filter {
            it.name.replace("_", " ", ignoreCase = true)
                .contains(event.trim(), ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add a Type", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = event,
            onValueChange = { onEventChange(it) },
            label = { Text("Search or type event type...") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        )

        // Show suggestions only if the field is focused and there are matches
        if (isFocused && suggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            ) {
                items(suggestions) { topic ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = topic.name.replace("_", " ")
                                    .lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            )
                        },
                        onClick = {
                            onEventChange(
                                topic.name.replace("_", " ")
                                    .lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            )
                            isFocused = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = onNextClicked,
            enabled = event.isNotBlank()
        ) {
            Text(text = "Next")
        }
    }
}

@Composable
fun CreateEventCaption(caption:String,onCaptionChange:(String)->Unit,onNextClicked:()->Unit){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Add Caption")
        OutlinedTextField(value =caption , onValueChange =onCaptionChange, modifier = Modifier.fillMaxWidth())
        Button(onClick =  onNextClicked ) {
            Text(text = "Next")
        }
    }
}

@Composable
fun YourLocation(onNextClicked: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Your Location")
        Text(text =ProfileObject.profile?.address!!, modifier = Modifier )
        Button(onClick =  onNextClicked ) {
            Text(text = "Next")
        }
    }
}

//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun CreateEventFinalScreen(uri: Uri?,caption: String,eventType:String) {
//    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally){
//        Text(text = ProfileObject.profile?.address!!)
//        GlideImage(model = uri, contentDescription ="", modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight(0.7f) )
//        Text(text = caption)
//        Text(text = eventType)
//
//
//    }
//}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CreateEventFinalScreen(
    uri: Uri?,
    caption: String,
    eventType: String,
    onPostClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🏠 User Address
        Text(
            text = ProfileObject.profile?.address ?: "Unknown location",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Start)
        )

        // 🖼️ Image Preview
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            if (uri != null) {
                GlideImage(
                    model = uri,
                    contentDescription = "Event Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No image selected", color = Color.White)
                }
            }
        }

        // ✍️ Caption Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Caption", style = MaterialTheme.typography.labelSmall)
            Text(
                text = caption,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // 📌 Event Type Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Event Type", style = MaterialTheme.typography.labelSmall)
            Text(
                text = eventType,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🚀 Post Button
        Button(
            onClick = onPostClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Post Event")
        }
    }
}
