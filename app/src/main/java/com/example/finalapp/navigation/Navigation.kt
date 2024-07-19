package com.example.finalapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalapp.screens.chat.ChatViewModel
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.screensUI.EnterOTPScreenUI

import com.example.finalapp.auth.screensUI.LoginScreenUI
import com.example.finalapp.auth.screensUI.SignupScreenUI
import com.example.finalapp.auth.screensUI.SplashScreenUI
import com.example.finalapp.auth.screensUI.otp.OtpBox
import com.example.finalapp.screens.chat.ChatScreenUI
import com.example.finalapp.auth.screensUI.FinalUserCreation
import com.example.finalapp.offer.OfferViewModel
import com.example.finalapp.screens.chat.ChatListScreen
import com.example.finalapp.screens.DirectChatScreenUI
import com.example.finalapp.screens.profile.GalleryPicker
import com.example.finalapp.screens.HomeScreenUI
import com.example.finalapp.screens.NotificationsScreenUI
import com.example.finalapp.screens.offer.CreateOfferScreen
import com.example.finalapp.screens.EventsAndPlacesScreen
import com.example.finalapp.screens.profile.ProfileScreenUI
import com.example.finalapp.screens.SettingsScreenUI
import com.example.finalapp.screens.TabView
import com.example.finalapp.screens.offer.PastRaisedOffer
import com.example.finalapp.screens.onboarding.screen.WelcomeScreen
import com.example.finalapp.screens.profile.ProfileViewModel
import com.google.accompanist.pager.ExperimentalPagerApi

const val NavArg="name"
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
    object SINGLE_CHAT:SCREENS("singleChat/{userNumber}")
    object  OTP2:SCREENS("otp")
    object GALLERY:SCREENS("gallery_picker")
    object WELCOME:SCREENS("welcome")
    object ALL_USERS:SCREENS("all_users")
    object SEARCH:SCREENS("search")
    object DROP_PROFILE:SCREENS("drop_profile")
    object CREATE_EVENT:SCREENS("raise_offer")
    object PAST_OFFERS:SCREENS("past_offers")
    object TABVIEW:SCREENS("tab_view")

}
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun Navigation(authViewModel: AuthViewModel, screen: String) {
    val navController:NavHostController= rememberNavController();
    val profileViewModel= hiltViewModel<ProfileViewModel>()
    val viewModel = hiltViewModel<ChatViewModel>()
    val offerViewModel= hiltViewModel<OfferViewModel>()



    NavHost(navController = navController, startDestination =SCREENS.HOME.route){
        composable(SCREENS.SPLASH.route){
            SplashScreenUI(navController,screen)
        }
        composable(SCREENS.LOGIN.route){
            LoginScreenUI(navController,authViewModel)
        }
        composable(SCREENS.SIGNUP.route){
            SignupScreenUI(navController)
        }
        composable(SCREENS.OTP.route){
           EnterOTPScreenUI(navController)
        }
        composable(SCREENS.FINALUSERCREATION.route){
            FinalUserCreation(authViewModel,navController)
        }
        composable(SCREENS.HOME.route){
            HomeScreenUI( navController,profileViewModel,authViewModel)
        }

        composable(SCREENS.PROFILE.route){
            ProfileScreenUI(navController,profileViewModel,authViewModel)
        }
        composable(SCREENS.SETTINGS.route){
            SettingsScreenUI(navController)
        }
        composable(SCREENS.NOTIFICATIONS.route){
            NotificationsScreenUI(navController)
        }
        composable(SCREENS.CHAT.route){
            ChatListScreen(navController)
        }
        composable(SCREENS.SINGLE_CHAT.route, arguments = listOf(navArgument("userNumber"){type= NavType.StringType}))
          {navBackStackEntry->
            val userNumber=navBackStackEntry.arguments?.getString("userNumber")
            ChatScreenUI(userNumber, navController, viewModel )

        }
        composable(SCREENS.OTP2.route){
            OtpBox()
        }
        composable(SCREENS.GALLERY.route){
            GalleryPicker(navController, profileViewModel )
        }
        composable(SCREENS.WELCOME.route){
            WelcomeScreen(navController)
        }
        composable(SCREENS.ALL_USERS.route){
            //AllProfiles(profileViewModel)
        }
        composable(SCREENS.SEARCH.route){
            EventsAndPlacesScreen(navController)
        }
        composable(SCREENS.DROP_PROFILE.route){
            DirectChatScreenUI(navController,offerViewModel )
        }
        composable(SCREENS.CREATE_EVENT.route){
            CreateOfferScreen(authViewModel, offerViewModel, profileViewModel,navController )
        }
        composable(SCREENS.PAST_OFFERS.route){
            PastRaisedOffer(authViewModel = authViewModel, offerViewModel =offerViewModel , navController = navController)
        }
        composable(SCREENS.TABVIEW.route){
            TabView()
        }
    }



}