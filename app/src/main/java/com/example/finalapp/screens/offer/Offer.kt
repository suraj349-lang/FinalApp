package com.example.finalapp.screens.offer

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.model.OfferModel
import com.example.finalapp.offer.OfferViewModel
import com.example.finalapp.screens.OfferResponseDataAndAction
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun OfferScreenUI(navController: NavHostController= NavHostController(LocalContext.current)) {
   val offerViewModel:OfferViewModel= hiltViewModel<OfferViewModel>()
    var key by remember { mutableStateOf(0) }
    val scope= rememberCoroutineScope()

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
                    Button(onClick = {

                        scope.launch {
                            offerViewModel.createOffer(OfferModel("8765vnbm","65692fe3d203dbf0e3ddcb17"));
                            key = 1;
                        }
                    }) {
                        Text(text = "CREATE OFFER")
                    }



                }
                if(key==1){
                    Log.d("Data received","runned this")
                    OfferResponseDataAndAction(offerViewModel,navController);
                    key=0;
                }
                
            }

        }


    }

@Composable
fun SubmitButton(navController: NavController, viewModel: OfferViewModel) {


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