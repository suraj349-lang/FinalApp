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
import androidx.navigation.navDeepLink
import com.example.finalapp.enums.ImageUploadScreens
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.viewmodels.ChatViewModel
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.screens.auth.EnterOTPScreenUI

import com.example.finalapp.screens.auth.SplashScreenUI
import com.example.finalapp.screens.auth.util.OtpBox
import com.example.finalapp.screens._6chat.SingleChatScreenUI
import com.example.finalapp.screens.auth.FinalUserCreation
import com.example.finalapp.qrScanning.QRScannerScreen
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.EventsDetailsVerticalWrapper
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.templates.xhmaslive.XHamsLiveScreenWrapper
import com.example.finalapp.screens.pings.CameraPingScreen
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.screens._6chat.ChatListScreen
import com.example.finalapp.screens._4profile.GalleryPicker
import com.example.finalapp.screens._1home.HomeScreenUI
import com.example.finalapp.screens._1home.eventWarScreen.CommentsScreen
import com.example.finalapp.screens._8notification.NotificationScreenUI
import com.example.finalapp.screens._3createEventOrPing.createEvent.CreateEventMainScreen
import com.example.finalapp.screens._5settings.SettingsScreenUI
import com.example.finalapp.testing.TabView
import com.example.finalapp.screens._3createEventOrPing.PastRaisedOffer
import com.example.finalapp.screens._2pings.PingDetailsScreen
import com.example.finalapp.screens._2pings.PingScreenFinal
import com.example.finalapp.screens._3createEventOrPing.createPing.CreatePingWrapper
import com.example.finalapp.screens._4profile.ProfileScreenNew
import com.example.finalapp.screens._4profile.UserPublicProfile
import com.example.finalapp.screens._4profile.dropProfileUserProfile.DropProfileUserProfile
import com.example.finalapp.screens._4profile.privateUsername.PrivateUserNameScreenWrapper
import com.example.finalapp.screens._5settings.BlockedUsers
import com.example.finalapp.screens._5settings.BugExplanationScreen
import com.example.finalapp.screens._5settings.BugsAndSuggestion
import com.example.finalapp.screens._5settings.ClearSearchHistory
import com.example.finalapp.screens._5settings.DeleteAccount
import com.example.finalapp.screens._5settings.EditName
import com.example.finalapp.screens._5settings.EditPassword
import com.example.finalapp.screens._5settings.EditPhoneNumber
import com.example.finalapp.screens._5settings.EditUserName
import com.example.finalapp.screens._5settings.FeaturesListScreen
import com.example.finalapp.screens._5settings.HelpCentre
import com.example.finalapp.screens._5settings.Logout
import com.example.finalapp.screens._5settings.MyData
import com.example.finalapp.screens._5settings.PermissionsUI
import com.example.finalapp.screens._5settings.SafetyAndPrivacy
import com.example.finalapp.screens._5settings.SavedLoginInfo
import com.example.finalapp.screens.auth.LoginScreenWrapperNewUI
import com.example.finalapp.screens.auth.SignupScreenNewUI
import com.example.finalapp.screens.common.CameraXScreen
import com.example.finalapp.screens.common.ImagePreviewScreen
import com.example.finalapp.screens.onboarding.screen.WelcomeScreen
import com.example.finalapp.screens.pings.EditScreen
import com.example.finalapp.screens.pings.templates.PingTemplateSelector
import com.example.finalapp.screens.pings.templates.visualPingTemplates
import com.example.finalapp.screens.webrtc.OmegleScreen
import com.example.finalapp.viewmodels.ImageUploadViewModel
import com.example.finalapp.viewmodels.NotificationViewModel
import com.example.finalapp.viewmodels.SettingsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun Navigation(authViewModel: AuthViewModel, screen: String) {
    val navController:NavHostController= rememberNavController();
    val imageUploadViewModel= hiltViewModel<ImageUploadViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val eventsViewModel= hiltViewModel<EventsViewModel>()
    val chatViewModel= hiltViewModel<ChatViewModel>()
    val notificationViewModel= hiltViewModel<NotificationViewModel>()

    NavHost(navController = navController, startDestination =SCREENS.SPLASH.route){

        composable(SCREENS.SPLASH.route){
            SplashScreenUI(navController,screen)
        }
        composable(SCREENS.LOGIN.route){
            //LoginScreenUI(navController,authViewModel)
            LoginScreenWrapperNewUI(authViewModel, navController)
        }
        composable(SCREENS.SIGNUP.route){
           // SignupScreenUI(navController)
            SignupScreenNewUI(
                name = authViewModel.name.value,
                onNameChange = {authViewModel.name.value=it},
                password = authViewModel.password.value,
                onPasswordChange = {authViewModel.password.value=it},
                confirmPassword = authViewModel.confirmPassword.value,
                onConfirmPasswordChange = {authViewModel.confirmPassword.value=it},
                onBackClicked = {navController.navigate(SCREENS.LOGIN.route)},
                onSignInClicked = {navController.navigate(SCREENS.LOGIN.route)},
                onNextClicked = { navController.navigate(SCREENS.OTP.route)}
            )
        }
        composable(SCREENS.OTP.route){
           EnterOTPScreenUI(
               navController = navController,
               userName = authViewModel.username.value,
               onUserNameChange = {authViewModel.username.value=it},
               phoneNumber = authViewModel.phoneNumber.value,
               onPhoneNumberChange = {authViewModel.phoneNumber.value=it},
               otp = authViewModel.otp.value,
               onOtpChange = {authViewModel.otp.value=it},
               onSignUpClicked = {authViewModel.registerUser(RegisterUserModel.empty())}
           )
        }
        composable(SCREENS.FINALUSERCREATION.route){
            FinalUserCreation(authViewModel,navController)
        }
        composable(SCREENS.HOME.route){
            HomeScreenUI( navController,eventsViewModel,imageUploadViewModel,authViewModel,chatViewModel)
        }
        composable(SCREENS.PROFILE.route){
           // ProfileScreenUI(navController,imageUploadViewModel,authViewModel)
            ProfileScreenNew(navController,authViewModel, eventsViewModel, imageUploadViewModel)
        }
        composable(SCREENS.SETTINGS.route){
            SettingsScreenUI(navController,authViewModel)
        }
        composable(SCREENS.NOTIFICATIONS.route){
            NotificationScreenUI(navController,notificationViewModel)
        }
        composable(SCREENS.CHAT_LIST.route){
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
              SingleChatScreenUI(userName!!,chatListUserId!!, navController ,chatViewModel)
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
        composable(SCREENS.PINGS.route){
            //PingsScreenUI(navController,eventsViewModel)
            PingScreenFinal(navController,eventsViewModel)
        }
        composable(SCREENS.BETA.route){
            XHamsLiveScreenWrapper(eventsViewModel = eventsViewModel, navController = navController) {
                eventsViewModel.getAllEvents()
            }
        }

        composable(SCREENS.PAST_OFFERS.route){
            PastRaisedOffer(authViewModel = authViewModel, eventsViewModel =eventsViewModel , navController = navController)
        }
        composable(SCREENS.TABVIEW.route){
            //todo for testing purpose
            TabView(navController,imageUploadViewModel,authViewModel)
        }
        composable(SCREENS.CREATE_PING.route) {
          //  CreateEvent(eventsViewModel, navController)
            //CreatePing(authViewModel, eventsViewModel, navController)
           // CreatePingMainScreen(navController,eventsViewModel)
            CreatePingWrapper(navController,eventsViewModel)
        }
        composable(SCREENS.CREATE_EVENT.route, arguments = listOf(navArgument("parentEventId"){
            type= NavType.StringType
            nullable=true
            defaultValue=null
        })) {
            val parentEventId=it.arguments?.getString("parentEventId")
            CreateEventMainScreen(parentEventId,navController,eventsViewModel)
        }
        composable(route = SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.route, arguments = listOf(
            navArgument(name = "id"){
                type= NavType.StringType
            }
        )){navBackStackEntry->
            val id=navBackStackEntry.arguments?.getString("id") ?: ""
           // PublicEventDetailsScreenWrapper(id, navController,eventsViewModel,authViewModel)
            EventsDetailsVerticalWrapper(id, navController,eventsViewModel,authViewModel)
        }
        composable(SCREENS.PRIVATE_PROFILE.route){
            PrivateUserNameScreenWrapper(navController = navController)
        }
//        composable(SCREENS.TIKTOK.route){
//            val list= listOf<String>("1","2","3","4","5","6")
//            Tiktok(videos = list)
//        }
        // when the dropped profile is clicked then it is shown
        composable(route=SCREENS.DROP_PROFILE_USER_PROFILE.route, arguments = listOf(navArgument("dropProfileResponse"){ type= NavType.StringType })){navBackStackEntry ->
            val json=navBackStackEntry.arguments?.getString("dropProfileResponse")
            val dropProfileResponse=json?.let { Json.decodeFromString<DropProfileResponse>(it) }
            DropProfileUserProfile(navController,dropProfileResponse)

        }
        composable(route=SCREENS.PING_DETAILS.route, arguments = listOf(navArgument("pingResponse"){ type= NavType.StringType })){navBackStackEntry ->
            val json=navBackStackEntry.arguments?.getString("pingResponse")
            val pingResponse=json?.let { Json.decodeFromString<PingResponse>(it) }
            PingDetailsScreen(navController,pingResponse)

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

        composable(
            route=SCREENS.USER_PUBLIC_PROFILE.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://www.spint.com/profile?userId={userId}"
            }),
            arguments = listOf(navArgument("userId"){ type= NavType.StringType })){navBackStackEntry ->
            val userId=navBackStackEntry.arguments?.getString("userId")
            UserPublicProfile(eventsViewModel,navController,userId)

        }
        composable("ping"){
            CameraPingScreen(navController)
        }
        composable("edit"){
            EditScreen(navController)
        }
        composable("template"){
            PingTemplateSelector(templates = visualPingTemplates, onTemplateSelected ={} )
        }

        composable("qrcode"){
            QRScannerScreen()
        }


        composable(SCREENS.COMMENT.route){
            CommentsScreen()
        }
        composable(SCREENS.OMEGLE.route){
            OmegleScreen()
        }


        // SETTINGS---------------------------------------------------------------------------------------------------------------------------------------------------
        composable(SCREENS.EDIT_NAME.route){
            EditName(navController = navController,settingsViewModel)
        }
        composable(SCREENS.EDIT_USER_NAME.route){
            EditUserName(navController = navController,settingsViewModel)
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
            BugsAndSuggestion(navController = navController){

            }
        }
        composable(SCREENS.FEATURES_SCREEN.route){
            FeaturesListScreen({navController.navigate(SCREENS.BUG_EXPLANATION_SCREEN.route)}){
                navController.navigateUp()
            }
        }
        composable(SCREENS.BUG_EXPLANATION_SCREEN.route){
            BugExplanationScreen(){
                navController.navigateUp()
            }
        }
        composable(SCREENS.SAFETY_AND_PRIVACY.route){
            SafetyAndPrivacy(navController = navController)
        }
        composable(SCREENS.HELP_CENTRE.route){
            HelpCentre(navController = navController)
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