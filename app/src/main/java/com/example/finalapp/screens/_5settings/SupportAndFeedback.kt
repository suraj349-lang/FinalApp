package com.example.finalapp.screens._5settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.screens._5settings.legal.readRawTextFile
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun BugsAndSuggestion(navController:NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Bugs And Suggestion") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Update Your password", fontSize = 30.sp, fontFamily = Constants.DONGLE_BOLD)
            }
        }
    )

}
@Composable
fun SafetyAndPrivacy(navController:NavHostController) {
    val context= LocalContext.current
    val text by remember { mutableStateOf( readRawTextFile(context, R.raw.safetyandprivacy)) }
    Scaffold(topBar = {
        CommonTopBar(title = "Safety And Privacy") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                item {
                    Text(
                        text = text,
                      //  style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    )

}

@Composable
fun HelpCentre(navController:NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Help Centre") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Help Centre", fontSize = 30.sp, fontFamily = Constants.DONGLE_BOLD)
            }
        }
    )

}
