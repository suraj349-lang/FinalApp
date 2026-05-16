package com.spint.app.screens._3createEventOrFlashPost

import BottomBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spint.app.R
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.screens._1home.commonUI.HomeTopBar
import com.spint.app.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastRaisedOffer(authViewModel: AuthViewModel, homeViewModel: HomeViewModel, navController: NavHostController){
    val items:List<String> = listOf("Alpha-1","Beta-1","Gamma-1")
    val buttonsVisible = remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            HomeTopBar(
                scrollBehavior =TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = "Raised Offers",
                navController=navController,
                navIcon = false,
                actionIcon = false,
                icon = R.drawable.create_event
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("") },Modifier.size(75.dp), backgroundColor = Color.White, contentColor = Color.Black) {
                Icon(painter = painterResource(R.drawable.share), contentDescription = "", modifier = Modifier.size(40.dp))
            }
        }, floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
        Surface(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items.size) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Gray)
                    ) {
                        Text(
                            text = items[index],
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }


            }

        }
    }}