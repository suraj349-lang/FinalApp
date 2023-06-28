package com.example.finalapp.navigation

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalapp.auth.screensUI.EnterOTPScreenUI

import com.example.finalapp.auth.screensUI.LoginScreenUI
import com.example.finalapp.auth.screensUI.SignupScreenUI
import com.example.finalapp.screens.HomeScreenUI


sealed class SCREENS(val route:String){
    object LOGIN:SCREENS("login_Screen")
    object SIGNUP:SCREENS("signup_screen")
    object HOME:SCREENS("home_screen")
    object ENTER_OTP:SCREENS("enter-otp")

}
@Composable
fun Navigation(){
    val navController= rememberNavController();
    NavHost(navController = navController, startDestination =SCREENS.SIGNUP.route){
        composable(SCREENS.LOGIN.route){
         LoginScreenUI(navController)
        }
        composable(SCREENS.SIGNUP.route){
            SignupScreenUI(navController)
        }
        composable(SCREENS.ENTER_OTP.route){
           EnterOTPScreenUI(navController)
        }
        composable(SCREENS.HOME.route){
            HomeScreenUI(navController)
        }
    }



}