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
import com.example.finalapp.viewmodels.ChatViewModel
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.screens.auth.util.EnterOTPScreenUI

import com.example.finalapp.screens.auth.LoginScreenUI
import com.example.finalapp.screens.auth.SignupScreenUI
import com.example.finalapp.screens.auth.SplashScreenUI
import com.example.finalapp.screens.auth.util.OtpBox
import com.example.finalapp.screens._6chat.ChatScreenUI
import com.example.finalapp.screens.auth.FinalUserCreation
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.screens._6chat.ChatListScreen
import com.example.finalapp.screens._4profile.GalleryPicker
import com.example.finalapp.screens._1home.HomeScreenUI
import com.example.finalapp.screens._8notification.NotificationsScreenUI
import com.example.finalapp.screens._2search.SearchScreen
import com.example.finalapp.screens._3createEvent.CreateEvent
import com.example.finalapp.screens._4profile.ProfileScreenUI
import com.example.finalapp.screens._5settings.SettingsScreenUI
import com.example.finalapp.testing.TabView
import com.example.finalapp.screens._3createEvent.PastRaisedOffer
import com.example.finalapp.screens._3createEvent.PremiumCreateEvent
import com.example.finalapp.screens._4profile.ProfileScreenNew
import com.example.finalapp.screens.onboarding.screen.WelcomeScreen
import com.example.finalapp.testingp.Tiktok
import com.example.finalapp.viewmodels.ImageUploadViewModel
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
    object CREATE_EVENT:SCREENS("create_event")
    object PAST_OFFERS:SCREENS("past_offers")
    object TABVIEW:SCREENS("tab_view")
    object TIKTOK:SCREENS("tiktok")
    object PREMIUM_CREATE_EVENT:SCREENS("premium")

}
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun Navigation(authViewModel: AuthViewModel, screen: String) {
    val navController:NavHostController= rememberNavController();
    val imageUploadViewModel= hiltViewModel<ImageUploadViewModel>()
    val viewModel = hiltViewModel<ChatViewModel>()
    val eventsViewModel= hiltViewModel<EventsViewModel>()
    val chatViewModel= hiltViewModel<ChatViewModel>()

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
            HomeScreenUI( navController,eventsViewModel,imageUploadViewModel,authViewModel)
        }

        composable(SCREENS.PROFILE.route){
           // ProfileScreenUI(navController,imageUploadViewModel,authViewModel)
            ProfileScreenNew(navController,authViewModel, eventsViewModel, imageUploadViewModel)
        }
        composable(SCREENS.SETTINGS.route){
            SettingsScreenUI(navController)
        }
        composable(SCREENS.NOTIFICATIONS.route){
            NotificationsScreenUI(navController)
        }
        composable(SCREENS.CHAT.route){
            ChatListScreen(navController, chatViewModel)
        }
        composable(SCREENS.SINGLE_CHAT.route, arguments = listOf(navArgument("userNumber"){type= NavType.StringType}))
          {navBackStackEntry->
            val userNumber=navBackStackEntry.arguments?.getString("userNumber")
            ChatScreenUI(userNumber, navController ,chatViewModel)

        }
        composable(SCREENS.OTP2.route){
            OtpBox()
        }
        composable(SCREENS.GALLERY.route){
            GalleryPicker(navController, imageUploadViewModel )
        }
        composable(SCREENS.WELCOME.route){
            WelcomeScreen(navController)
        }
        composable(SCREENS.ALL_USERS.route){
            //AllProfiles(profileViewModel)
        }
        composable(SCREENS.SEARCH.route){
            SearchScreen(navController)
        }

        composable(SCREENS.PAST_OFFERS.route){
            PastRaisedOffer(authViewModel = authViewModel, eventsViewModel =eventsViewModel , navController = navController)
        }
        composable(SCREENS.TABVIEW.route){
            //todo for testing purpose
            TabView(navController,imageUploadViewModel,authViewModel)
        }

        composable(SCREENS.CREATE_EVENT.route) {
            CreateEvent(eventsViewModel, navController)
        }

        composable(SCREENS.PREMIUM_CREATE_EVENT.route) {
            PremiumCreateEvent(authViewModel, eventsViewModel, navController)
        }
        composable(SCREENS.TIKTOK.route){
            val list= listOf<String>("1","2","3","4","5","6")
            Tiktok(videos = list)
        }

    }



}