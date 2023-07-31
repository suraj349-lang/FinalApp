package com.example.finalapp.screens.offer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalapp.R


@Preview(showBackground = true)
@Composable
fun OfferScreenUI(navController: NavController= NavController(LocalContext.current)) {
   val viewModel:OfferViewModel= viewModel()

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(modifier = Modifier.size(width = 400.dp, height = 600.dp), shape = RoundedCornerShape(12.dp), color = Color(
                0xFFC0C3C4
            )
            ) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Your offer", fontWeight = FontWeight.Bold, color = Color.Black, style = MaterialTheme.typography.headlineMedium, fontFamily = FontFamily.Serif)
                    ImageSurface()
                    OfferRow()
                    DescriptionRow(viewModel)
                    SubmitButton(navController,viewModel)


                }
                
            }

        }


    }

@Composable
fun SubmitButton(navController: NavController, viewModel: OfferViewModel) {
    Button(onClick = { navController.popBackStack() }) {
        Text(text = "CREATE OFFER")
        
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionRow(viewModel: OfferViewModel) {

    OutlinedTextField(
        value =viewModel.descriptionText.value ,
        onValueChange ={viewModel.descriptionText.value=it},
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp).padding(start=4.dp, end=4.dp)
    )
}

@Composable
fun OfferRow() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Image(painter = painterResource(id = R.drawable.coffee_24), contentDescription ="Coffee", modifier = Modifier.size(40.dp) )
        Image(painter = painterResource(id = R.drawable.personal_24), contentDescription ="Personal" , modifier = Modifier.size(40.dp))
        Image(painter = painterResource(id = R.drawable.meet_24), contentDescription ="meet", modifier = Modifier.size(40.dp) )


        
    }
}

@Composable
fun ImageSurface(){
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 400.dp, height = 350.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFDDD8CB))
    {
        Image(painter = painterResource(id = R.drawable.hd_girl), contentDescription ="", contentScale = ContentScale.Crop )

    }

}