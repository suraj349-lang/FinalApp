package com.example.finalapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.screensUI.EnterOTPScreenUI

import com.example.finalapp.auth.screensUI.LoginScreenUI
import com.example.finalapp.auth.screensUI.SignupScreenUI
import com.example.finalapp.auth.screensUI.SplashScreenUI
import com.example.finalapp.auth.screensUI.otp.OtpBox
import com.example.finalapp.screens.ChatScreenUI
import com.example.finalapp.screens.FinalUserCreation
import com.example.finalapp.screens.HomeScreenUI
import com.example.finalapp.screens.NotificationsScreenUI
import com.example.finalapp.screens.ProfileScreenUI
import com.example.finalapp.screens.SettingsScreenUI



sealed class SCREENS(val route:String){
    object SPLASH:SCREENS("splash_Screen")
    object LOGIN:SCREENS("login_Screen")
    object SIGNUP:SCREENS("signup_screen")
    object HOME:SCREENS("home_screen")
    object PROFILE:SCREENS("profile_screen")
    object OTP:SCREENS("enter-otp")
    object FINALUSERCREATION:SCREENS("final_user_creation")
    object SETTINGS:SCREENS("settings_screen")
    object NOTIFICATIONS:SCREENS("notifications_screen")
    object CHAT:SCREENS("chat_screen")

    object  OTP2:SCREENS("otp")

}
@Composable
fun Navigation(){
    val navController:NavHostController= rememberNavController();
    NavHost(navController = navController, startDestination =SCREENS.LOGIN.route){
        composable(SCREENS.SPLASH.route){
            SplashScreenUI(navController)
        }
        composable(SCREENS.LOGIN.route){
            val authViewModel= hiltViewModel<AuthViewModel>()
            LoginScreenUI(navController,authViewModel)
        }
        composable(SCREENS.SIGNUP.route){
            SignupScreenUI(navController)
        }
        composable(SCREENS.OTP.route){
           EnterOTPScreenUI(navController)
        }
        composable(SCREENS.HOME.route){
            HomeScreenUI(navController)
        }
        composable(SCREENS.FINALUSERCREATION.route){
            FinalUserCreation(navController)
        }
        composable(SCREENS.PROFILE.route){
            ProfileScreenUI(navController)
        }
        composable(SCREENS.SETTINGS.route){
            SettingsScreenUI(navController)
        }
        composable(SCREENS.NOTIFICATIONS.route){
            NotificationsScreenUI(navController)
        }
        composable(SCREENS.CHAT.route){
            ChatScreenUI(navController)
        }
        composable(SCREENS.OTP2.route){
            OtpBox()
        }
    }



}