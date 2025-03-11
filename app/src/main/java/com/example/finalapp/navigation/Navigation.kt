package com.example.finalapp.navigation

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.model.User
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
import com.example.finalapp.screens._2search.TrendingScreen
import com.example.finalapp.screens._3createEvent.CreateEvent
import com.example.finalapp.screens._5settings.SettingsScreenUI
import com.example.finalapp.testing.TabView
import com.example.finalapp.screens._3createEvent.PastRaisedOffer
import com.example.finalapp.screens._3createEvent.PremiumCreateEvent
import com.example.finalapp.screens._4profile.ProfileScreenNew
import com.example.finalapp.screens._4profile.dropProfileUserProfile.DropProfileUserProfile
import com.example.finalapp.screens._5settings.BlockedUsers
import com.example.finalapp.screens._5settings.BugsAndSuggestion
import com.example.finalapp.screens._5settings.ClearSearchHistory
import com.example.finalapp.screens._5settings.DeleteAccount
import com.example.finalapp.screens._5settings.EditName
import com.example.finalapp.screens._5settings.EditPassword
import com.example.finalapp.screens._5settings.EditPhoneNumber
import com.example.finalapp.screens._5settings.EditUserName
import com.example.finalapp.screens._5settings.HelpCentre
import com.example.finalapp.screens._5settings.Logout
import com.example.finalapp.screens._5settings.MyData
import com.example.finalapp.screens._5settings.OtherLegal
import com.example.finalapp.screens._5settings.PermissionsUI
import com.example.finalapp.screens._5settings.PrivacyPolicyScreen
import com.example.finalapp.screens._5settings.SafetyAndPrivacy
import com.example.finalapp.screens._5settings.SafetyCentre
import com.example.finalapp.screens._5settings.SavedLoginInfo
import com.example.finalapp.screens._5settings.TermsOfService
import com.example.finalapp.screens.onboarding.screen.WelcomeScreen
import com.example.finalapp.testingp.Tiktok
import com.example.finalapp.viewmodels.ImageUploadViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    object DROP_PROFILE_USER_PROFILE:SCREENS("drop_profile_user_profile/{dropProfileResponse}"){
        fun passProfile(dropProfileResponse: DropProfileResponse):String{
            val profileJson=Uri.encode(Json.encodeToString(dropProfileResponse))
            return "drop_profile_user_profile/$profileJson"
        }
    }
    //-----------------SETTINGS-------------------------------------------------------------------------------------------
    object EDIT_NAME:SCREENS("edit_name")
    object EDIT_USER_NAME:SCREENS("edit_user_name")
    object PHONE_NUMBER:SCREENS("phone_number")
    object PASSWORD:SCREENS("password")
    object DELETE_ACCOUNT:SCREENS("delete_account")

    //-----------------------------------------------------------------//
    object BUGS_AND_SUGGESTION:SCREENS("bugs")
    object SAFETY_AND_PRIVACY:SCREENS("safety")
    object HELP_CENTRE:SCREENS("help_centre")
    //--------------------------------------------------------------------//
    object PRIVACY_POLICY:SCREENS("privacy_policy")
    object SAFETY_CENTRE:SCREENS("safety_centre")
    object TERMS_OF_SERVICE:SCREENS("terms_of_service")
    object OTHER_LEGAL:SCREENS("other_legal")

    //-----------------------------------------------------------------//
    object CLEAR_SEARCH_HISTORY:SCREENS("clear_search_history")
    object PERMISSIONS:SCREENS("permissions")
    object BLOCKED_USERS:SCREENS("blocked_users")
    object SAVED_LOGIN_INFO:SCREENS("saved_login_info")
    object MY_DATA:SCREENS("my_data")
    object LOG_OUT:SCREENS("log_out")



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
            SettingsScreenUI(navController,authViewModel)
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
            TrendingScreen(navController)
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
        composable(route=SCREENS.DROP_PROFILE_USER_PROFILE.route, arguments = listOf(navArgument("dropProfileResponse"){ type= NavType.StringType })){navBackStackEntry ->
            val json=navBackStackEntry.arguments?.getString("dropProfileResponse")
            val dropProfileResponse=json?.let { Json.decodeFromString<DropProfileResponse>(it) }
            DropProfileUserProfile(navController,dropProfileResponse)

        }
        
        
        // SETTINGS---------------------------------------------------------------------------------------------------------------------------------------------------
        composable(SCREENS.EDIT_NAME.route){
            EditName(navController = navController)
        }
        composable(SCREENS.EDIT_USER_NAME.route){
            EditUserName(navController = navController)
        }
        composable(SCREENS.PASSWORD.route){
            EditPassword(navController = navController)
        }
        composable(SCREENS.PHONE_NUMBER.route){
            EditPhoneNumber(navController = navController)
        }
        composable(SCREENS.DELETE_ACCOUNT.route){
            DeleteAccount(navController = navController)
        }
        composable(SCREENS.BUGS_AND_SUGGESTION.route){
            BugsAndSuggestion(navController = navController)
        }
        composable(SCREENS.SAFETY_AND_PRIVACY.route){
            SafetyAndPrivacy(navController = navController)
        }
        composable(SCREENS.HELP_CENTRE.route){
            HelpCentre(navController = navController)
        }

        //---------------------------------------------------------------------------------//
        composable(SCREENS.PRIVACY_POLICY.route){
            PrivacyPolicyScreen(navController = navController)
        }
        composable(SCREENS.SAFETY_CENTRE.route){
            SafetyCentre(navController = navController)
        }
        composable(SCREENS.TERMS_OF_SERVICE.route){
            TermsOfService(navController = navController)
        }
        composable(SCREENS.OTHER_LEGAL.route){
            OtherLegal(navController = navController)
        }
        //---------------------------------------------------------------------------------//
        composable(SCREENS.CLEAR_SEARCH_HISTORY.route){
            ClearSearchHistory(navController = navController)
        }
        composable(SCREENS.PERMISSIONS.route){
            PermissionsUI(navController = navController)
        }
        composable(SCREENS.BLOCKED_USERS.route){
            BlockedUsers(navController = navController)
        }
        composable(SCREENS.SAVED_LOGIN_INFO.route){
            SavedLoginInfo(navController = navController)
        }
        composable(SCREENS.MY_DATA.route){
            MyData (navController = navController)
        }
        composable(SCREENS.LOG_OUT.route){
            Logout(navController = navController)
        }

    }



}