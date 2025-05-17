package com.example.finalapp.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalapp.enums.ImageUploadScreens
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.viewmodels.ChatViewModel
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.loginActivity.auth.util.EnterOTPScreenUI

import com.example.finalapp.loginActivity.auth.LoginScreenUI
import com.example.finalapp.loginActivity.auth.SignupScreenUI
import com.example.finalapp.loginActivity.auth.SplashScreenUI
import com.example.finalapp.loginActivity.auth.util.OtpBox
import com.example.finalapp.screens._6chat.ChatScreenUI
import com.example.finalapp.loginActivity.auth.FinalUserCreation
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.screens._6chat.ChatListScreen
import com.example.finalapp.screens._4profile.GalleryPicker
import com.example.finalapp.screens._1home.HomeScreenUI
import com.example.finalapp.screens._8notification.NotificationsScreenUI
import com.example.finalapp.screens._2search.TrendingScreen
import com.example.finalapp.screens._3createEvent.CreateEventNew
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
import com.example.finalapp.screens.common.CameraXScreen
import com.example.finalapp.screens.common.ImagePreviewScreen
import com.example.finalapp.screens.onboarding.screen.WelcomeScreen
import com.example.finalapp.testingDataAndScreen.Tiktok
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.viewmodels.ImageUploadViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun Navigation(authViewModel: AuthViewModel, screen: String) {
    val navController:NavHostController= rememberNavController();
    val imageUploadViewModel= hiltViewModel<ImageUploadViewModel>()
    val viewModel = hiltViewModel<ChatViewModel>()
    val eventsViewModel= hiltViewModel<EventsViewModel>()
    val chatViewModel= hiltViewModel<ChatViewModel>()

    NavHost(navController = navController, startDestination =SCREENS.LOGIN.route){
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
        composable(SCREENS.SINGLE_CHAT.route,
            arguments = listOf(
                navArgument("userName"){type= NavType.StringType},
                navArgument("chatListUserId"){type= NavType.StringType}
            ))
          {navBackStackEntry->
              val userName=navBackStackEntry.arguments?.getString("userName")
              val chatListUserId=navBackStackEntry.arguments?.getString("chatListUserId")
              ChatScreenUI(userName!!,chatListUserId!!, navController ,chatViewModel)
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
        composable(SCREENS.CREATE_EVENT_PRIVATE.route) {
          //  CreateEvent(eventsViewModel, navController)
            CreateEventNew(navController,eventsViewModel)
        }
        composable(SCREENS.CREATE_EVENT_PUBLIC.route) {
            PremiumCreateEvent(authViewModel, eventsViewModel, navController)
        }
        composable(SCREENS.TIKTOK.route){
            val list= listOf<String>("1","2","3","4","5","6")
            Tiktok(videos = list)
        }
        // when the dropped profile is clicked then it is shown
        composable(route=SCREENS.DROP_PROFILE_USER_PROFILE.route, arguments = listOf(navArgument("dropProfileResponse"){ type= NavType.StringType })){navBackStackEntry ->
            val json=navBackStackEntry.arguments?.getString("dropProfileResponse")
            val dropProfileResponse=json?.let { Json.decodeFromString<DropProfileResponse>(it) }
            DropProfileUserProfile(navController,dropProfileResponse)

        }
        composable("camerax/{screen}"){backStackEntry->
            val lastScreen=backStackEntry.arguments?.getString("screen") ?: ""
            CameraXScreen(navController = navController,lastScreen)
        }
        composable("preview/{screen}/{encodedUri}") { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("encodedUri") ?: ""
            val uri = Uri.parse(Uri.decode(encodedUri))
            val lastScreen = backStackEntry.arguments?.getString("screen") ?: ""
            ImagePreviewScreen(
                uri = uri,
                lastScreen=lastScreen,
                imageUploadViewModel=imageUploadViewModel,
                eventsViewModel=eventsViewModel,
                onDoneClicked = {
                    when(lastScreen){
                        ImageUploadScreens.PROFILE.screen->{
                            imageUploadViewModel.startProfileImageUpload.value=true
                            navController.navigate(SCREENS.PROFILE.route)
                        }
                        ImageUploadScreens.CREATE_EVENT.screen->{}
                        else->{
                            navController.navigate(SCREENS.HOME.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            eventsViewModel.showDropDialog.value=true
                        }
                    }

                }
            ) { navController.navigateUp() }
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
//        composable(SCREENS.IMAGE_CROPPER.route){
//            AutoImageCropper()
//        }


    }



}