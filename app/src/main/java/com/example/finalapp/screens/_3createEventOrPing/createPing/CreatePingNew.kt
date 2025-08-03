package com.example.finalapp.screens._3createEventOrPing.createPing

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.model.pings.PingRequestDto
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.uriToFile
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.UserLocationObject
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.EventsViewModel
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePingWrapper(navController: NavHostController, eventsViewModel: EventsViewModel) {

    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    val expiration by remember {
        mutableStateOf(12)
    }
    val isActive by remember {
        derivedStateOf {  title.isNotEmpty()}
    }
    val userLocation by UserLocationObject.userLocation.collectAsState()

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri=uri
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }




    val context = LocalContext.current
    val createPing by eventsViewModel.createPingResponse.collectAsState()
    when(createPing){
        is RequestState.Idle ->{}
        is RequestState.Error ->{
            Toast.makeText(LocalContext.current,"Error creating ping", Toast.LENGTH_SHORT).show()
            navController.navigate(SCREENS.HOME.route){
                popUpTo(0)
            }
        }
        is RequestState.Success->{

            Toast.makeText(LocalContext.current,"Ping created successfully", Toast.LENGTH_SHORT).show()
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
            CreatePingTopNew(isActive) {
                if (title.isNotEmpty()) {
                    val uri = imageUri
                    var imageFile by mutableStateOf<File?>(null)
                    if (uri != Uri.EMPTY) {
                        imageFile = uriToFile(uri!!, context)
                    } else return@CreatePingTopNew
                    imageFile?.let {
                        eventsViewModel.uploadImageAndThenCreateEvent(
                            UserObject.user.value.user ,
                            it
                        ) { imageKey ->
                            eventsViewModel.createPing(
                                PingRequestDto(
                                    user = UserObject.user.value.user ,
                                    userName = UserObject.user.value.userName,
                                    title = title,
                                    image = imageKey,
                                    location = userLocation.address.toString(),
                                    description = description,
                                    expirationTime = expiration.toString()
                                )
                            )
                        }
                    }

                }
            }
        },
        bottomBar = {},
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = Color.White
            ) {
                CreatePing(
                    title=title,
                    onTitleChange = { newTitle -> title = newTitle },
                    description=description,
                    onDescriptionChange = { newDescription -> description = newDescription },
                    imageUri = imageUri,
                    onGalleryClicked={pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))},
                    onCameraClicked={},
                )
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePing(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    imageUri:Uri?,
    onGalleryClicked:()->Unit,
    onCameraClicked:()->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Add media", fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp, color = Color.Black)
        MediaAddition(onGalleryClicked,onCameraClicked)
        TitleTextSpace("Title", title) { onTitleChange(it) }
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
        AddCategory()
        AddTag()
        DescriptionTextSpace("description", description) { onDescriptionChange(it) }
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
        Deadline()
        SelectedImage(imageUri)
    }
}

@Composable
fun SelectedImage(imageUri:Uri?) {
    AsyncImage(model =imageUri , contentDescription = "", modifier = Modifier
        .width(100.dp)
        .aspectRatio(9f / 16f), contentScale = ContentScale.Fit)
}


@Composable
fun MediaAddition(onGalleryClicked: () -> Unit, onCameraClicked: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)){
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Image(painter = painterResource(id = R.drawable.add_photo), contentDescription ="", modifier = Modifier
                .size(32.dp)
                .clickable { onGalleryClicked() })
            Image(painter = painterResource(id = R.drawable.color_camera), contentDescription ="", modifier = Modifier
                .size(32.dp)
                .clickable { onCameraClicked() })
            Image(painter = painterResource(id = R.drawable.add_link), contentDescription ="", modifier = Modifier.size(32.dp))
        }
    }

}

@Composable
fun TitleTextSpace(hint:String,text:String,onTextChange:(String)->Unit) {
    Box {
        if (text.isEmpty()) {
            Text(
                text = hint,
                color = Color.Black,
                fontSize = 28.sp,
                fontFamily = Constants.FONT_MEDIUM,
                fontWeight = FontWeight.Bold
            )
        }

        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 28.sp,
                fontFamily = Constants.FONT_MEDIUM
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}
@Composable
fun AddTag() {
    Card(modifier = Modifier
        .padding(top = 8.dp)
        .wrapContentWidth()
        .height(30.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
        Column(modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .padding(horizontal = 4.dp), verticalArrangement = Arrangement.Center) {
            Text(text = "+ Add tags & flairs (optional)", fontFamily = Constants.USER_NAME_FONT, fontSize = 9.sp, fontWeight = FontWeight.Bold,color= Color.Black)
        }

        
    }
}
@Composable
fun AddCategory() {
    Card(modifier = Modifier
        .wrapContentWidth()
        .height(30.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
        Row(modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .padding(horizontal = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.category), contentDescription ="", modifier = Modifier.size(20.dp))
            Text(text = "Select a category ", fontFamily = Constants.USER_NAME_FONT, fontSize = 10.sp, fontWeight = FontWeight.Bold,color= Color.Black)
            Image(painter = painterResource(id = R.drawable.unfold), contentDescription ="", modifier = Modifier.size(20.dp))
        }


    }
}


@Composable
fun DescriptionTextSpace(hint:String,description:String,onTextChange:(String)->Unit) {
    Box(modifier = Modifier
        .wrapContentSize()
        .padding(top = 16.dp)) {
        if (description.isEmpty()) {
            Text(
                text = hint,
                color = Color.Gray,
                fontSize = 18.sp,
                fontFamily = Constants.FONT_LIGHT,
                fontWeight = FontWeight.SemiBold
            )
        }

        BasicTextField(
            value = description,
            onValueChange = onTextChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Constants.FONT_MEDIUM
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Deadline() {
    var selectedDuration by remember { mutableStateOf("1 day") }
    var customDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    val formattedCustom = customDateTime?.format(DateTimeFormatter.ofPattern("MMM d, h:mm a"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Active till:",
            fontSize = 16.sp,
            fontFamily = Constants.FONT_MEDIUM,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF06146B)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(40.dp), horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier
                .wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
                Text(text = "1 Day", fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.Black)
            }
            Card(modifier = Modifier
                .wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
                Text(text = "1 Week", fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.Black)
            }
            Card(modifier = Modifier
                .wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
                Text(text = "1 Month", fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.Black)
            }
            Card(modifier = Modifier
                .wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
                Text(text = "Custom", fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.Black)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDurationDropdown(
    selectedDuration: String,
    onDurationSelected: (String) -> Unit,
    onCustomTimeSelected: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val durations = listOf("1 day", "1 week", "1 month", "Custom")

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var tempDate by remember { mutableStateOf<LocalDate?>(null) }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            OutlinedTextField(
                value = selectedDuration,
                onValueChange = {},
                readOnly = true,
                label = { Text("Duration") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF00D26A),
                    unfocusedBorderColor = Color.Gray,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                )
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF2A2A2A))
        ) {
            durations.forEach { duration ->
                DropdownMenuItem(
                    text = { Text(duration, color = Color.White) },
                    onClick = {
                        expanded = false
                        if (duration == "Custom") {
                            showDatePicker = true
                        } else {
                            onDurationSelected(duration)
                        }
                    }
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = {
                    tempDate = it
                    showTimePicker = true
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDateSelected: (LocalDate) -> Unit, onDismiss: () -> Unit) {
    val datePickerState = rememberDatePickerState()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(date)
                }
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            DatePicker(state = datePickerState)
        }
    )
}

@Composable
fun CreatePingTopNew(isActive:Boolean, onNext: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(color = Color.White)){
        Row(modifier= Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Image(
                painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        if (isActive) {
                            onNext()
                        }
                    },
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(containerColor = if(isActive) Color(0xFF081670) else Color.LightGray)
            ) {
                Text(
                    text = "Next ->",
                    fontFamily = Constants.FONT_MEDIUM,
                    color = if(isActive ) Color.White else Color.DarkGray,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 18.sp
                )
            }
        }
    }
}
