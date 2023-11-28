package com.example.finalapp.navigation

import androidx.compose.foundation.interaction.HoverInteraction
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
import com.example.finalapp.screens.ChatScreenUI
import com.example.finalapp.screens.FinalUserCreation
import com.example.finalapp.screens.HomeScreenUI
import com.example.finalapp.screens.NotificationsScreenUI
import com.example.finalapp.screens.ProfileScreenUI
import com.example.finalapp.screens.SettingsScreenUI
import com.example.finalapp.screens.offer.OfferRow
import com.example.finalapp.screens.offer.OfferScreenUI


sealed class SCREENS(val route:String){
    object SPLASH:SCREENS("splash_Screen")
    object LOGIN:SCREENS("login_Screen")
    object SIGNUP:SCREENS("signup_screen")
    object HOME:SCREENS("home_screen")
    object PROFILE:SCREENS("profile_screen")
    object ENTER_OTP:SCREENS("enter-otp")
    object FINALUSERCREATION:SCREENS("final_user_creation")
    object OFFER:SCREENS("offer_creation")
    object SETTINGS:SCREENS("settings_screen")
    object NOTIFICATIONS:SCREENS("notifications_screen")
    object CHAT:SCREENS("chat_screen")

}
@Composable
fun Navigation(){
    val navController:NavHostController= rememberNavController();
    NavHost(navController = navController, startDestination =SCREENS.SPLASH.route){
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
        composable(SCREENS.ENTER_OTP.route){
           EnterOTPScreenUI(navController)
        }
        composable(SCREENS.HOME.route){
            HomeScreenUI(navController)
        }
        composable(SCREENS.FINALUSERCREATION.route){
            val authViewModel= hiltViewModel<AuthViewModel>()
            FinalUserCreation(navController,authViewModel)
        }
        composable(SCREENS.OFFER.route){
            OfferScreenUI(navController)
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
    }



}