package com.example.finalapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


sealed class SCREENS(val route:String){
    object LOGIN:SCREENS("login_Screen")
    object SIGNUP:SCREENS("signup_screen")
    object HOME:SCREENS("home_screen")

}
@Composable
fun Navigation(){
    val navController= rememberNavController();
    NavHost(navController = navController, startDestination =SCREENS.LOGIN.route){
        composable(SCREENS.LOGIN.route){

        }
        composable(SCREENS.SIGNUP.route){

        }
        composable(SCREENS.HOME.route){

        }
    }



}