package com.example.finalapp.navigation

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalapp.auth.screensUI.EnterOTPScreenUI

import com.example.finalapp.auth.screensUI.LoginScreenUI
import com.example.finalapp.auth.screensUI.SignupScreenUI
import com.example.finalapp.screens.FinalUserCreation
import com.example.finalapp.screens.HomeScreenUI


sealed class SCREENS(val route:String){
    object LOGIN:SCREENS("login_Screen")
    object SIGNUP:SCREENS("signup_screen")
    object HOME:SCREENS("home_screen")
    object ENTER_OTP:SCREENS("enter-otp")
    object FINALUSERCREATION:SCREENS("final_user_creation")

}
@Composable
fun Navigation(){
    val navController= rememberNavController();
    NavHost(navController = navController, startDestination =SCREENS.HOME.route){
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
        composable(SCREENS.FINALUSERCREATION.route){
            FinalUserCreation(navController)
        }
    }



}