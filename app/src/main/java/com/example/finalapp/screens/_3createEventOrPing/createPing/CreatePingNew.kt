package com.example.finalapp.screens._3createEventOrPing.createPing

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalapp.R
import com.example.finalapp.model.pings.PingRequestDto
import com.example.finalapp.screens.dialogBox.uriToFile
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.EventsViewModel
import java.io.File
import javax.inject.Inject

@Preview(showBackground = true)
@Composable
fun CreatePingWrapper() {

    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    val imageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    val expiration by remember {
        mutableStateOf(12)
    }
    val isActive by remember {
        mutableStateOf(title.isNotEmpty() && description.isNotEmpty())
    }

    val context= LocalContext.current

    Scaffold(
        topBar = {
            CreatePingTopNew(isActive,{})
                 },
        bottomBar = {},
        content = {
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                CreatePing(title,{newTitle->title=newTitle},description,{newDescription->description=newDescription},imageUri){
                    val uri = imageUri
                    var imageFile by mutableStateOf<File?>(null)
                    if(uri != Uri.EMPTY) imageFile = uriToFile(uri!!, context )
                    imageFile?.let {
//                        eventsViewModel.uploadImageAndThenCreateEvent(ProfileObject.profile?.userId!!, it){ imageKey->
//                            eventsViewModel.createPing(
//                                PingRequestDto(
//                                    user = ProfileObject.profile?.userId!!,
//                                    userName = ProfileObject.profile?.username!!,
//                                    title=title,
//                                    image = imageKey,
//                                    location = location,
//                                    description =description,
//                                    expirationTime = expiration
//                                )
//                            )
//                        }
                    }
                }
            }
        }
    ) 
}

@Composable
fun CreatePingTopNew(isActive:Boolean,onCreatePost: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)){
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
                            onCreatePost()
                        }
                    },
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Text(
                    text = "Post",
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 18.sp
                )

            }

            
        }
    }
}


@Composable
fun CreatePing(title:String,onTitleChange:(String)->Unit,description: String,onDescriptionChange:(String)->Unit,imageUri:Uri,onCreatePost:()->Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Add media", fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp)
            MediaAddition()
            TitleTextSpace("Title", title) { onTitleChange(it) }
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
            AddCategory()
            AddTag()
            DescriptionTextSpace("description",description){onDescriptionChange(it)}
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
            Deadline()
        }
}


@Composable
fun MediaAddition() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)){
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Image(painter = painterResource(id = R.drawable.add_photo), contentDescription ="", modifier = Modifier.size(32.dp))
            Image(painter = painterResource(id = R.drawable.color_camera), contentDescription ="", modifier = Modifier.size(32.dp))
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
    Card(modifier = Modifier.padding(top=8.dp)
        .wrapContentWidth()
        .height(30.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
        Column(modifier = Modifier.wrapContentWidth().fillMaxHeight().padding(horizontal = 4.dp), verticalArrangement = Arrangement.Center) {
            Text(text = "+ Add tags & flairs (optional)", fontFamily = Constants.USER_NAME_FONT, fontSize = 9.sp, fontWeight = FontWeight.Bold,color= Color.Black)
        }

        
    }
}
@Composable
fun AddCategory() {
    Card(modifier = Modifier
        .wrapContentWidth()
        .height(30.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
        Row(modifier = Modifier.wrapContentWidth().fillMaxHeight().padding(horizontal = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
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



@Composable
fun Deadline() {
    var selectedDuration by remember { mutableStateOf("1 hour") }
    Box(modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth()
        .wrapContentHeight()){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Active till :", fontSize = 16.sp, fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.Bold,color = Color(0xFF06146B))

            TimeDurationDropdown(
                selectedDuration = selectedDuration,
                onDurationSelected = { selectedDuration = it },
                modifier = Modifier
                    .width(150.dp)
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDurationDropdown(
    selectedDuration: String,
    onDurationSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val durations = listOf("1 hour", "2 hours", "4 hours", "8 hours", "24 hours")

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedDuration,
            onValueChange = {onDurationSelected(it)},
            readOnly = true,
            label = { Text("Duration") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .clickable { expanded = true },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF00D26A),
                unfocusedBorderColor = Color.Gray,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF2A2A2A))
        ) {
            durations.forEach { duration ->
                DropdownMenuItem(
                    text = { Text(duration, color = Color.White) },
                    onClick = {
                        onDurationSelected(duration)
                        expanded = false
                    }
                )
            }
        }
    }
}
