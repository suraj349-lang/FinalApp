package com.spint.app.navigation

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.spint.app.enums.ImageUploadScreens
import com.spint.app.model.DropProfileResponse
import com.spint.app.model.RegisterUserModel
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.viewmodels.ChatViewModel
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.screens.auth.createAccount.ChooseUserNameAndCreateAccountScreen

import com.spint.app.screens.auth.SplashScreenUI
import com.spint.app.screens.auth.util.OtpBox
import com.spint.app.screens._6chat.SingleChatScreenUI
import com.spint.app.screens.auth.notUsed.FinalUserCreation
import com.spint.app.qrScanning.QRScannerScreen
import com.spint.app.screens._2Events.events.EventsScreenWrapper
import com.spint.app.screens._2Events.events.EventsDetailsVerticalWrapper
import com.spint.app.screens.pings.CameraPingScreen
import com.spint.app.viewmodels.HomeViewModel
import com.spint.app.screens._6chat.ChatListScreen
import com.spint.app.screens._4profile.GalleryPicker
import com.spint.app.screens._1home.HomeScreenUI
import com.spint.app.screens._1home._1FlashPosts.presentation.util.FlashPostCommentScreen
import com.spint.app.screens._8notification.NotificationScreenUI
import com.spint.app.screens._3createEventOrPing.createEvent.CreateEventMainScreen
import com.spint.app.screens._5settings.SettingsScreenUI
import com.spint.app.testing.TabView
import com.spint.app.screens._3createEventOrPing.PastRaisedOffer
import com.spint.app.screens._1home._1FlashPosts.presentation.view.detailsScreen.FlashPostDetailsScreen
import com.spint.app.screens._1home._1FlashPosts.presentation.view.FlashPostsScreen
import com.spint.app.screens._3createEventOrPing.createPing.CreatePingWrapper
import com.spint.app.screens._4profile.ProfileScreenNew
import com.spint.app.screens._4profile.dropProfileUserProfile.UserPublicProfile
import com.spint.app.screens._4profile.dropProfileUserProfile.DropProfileUserProfile
import com.spint.app.screens._4profile.privateUsername.PrivateProfileScreenWrapper
import com.spint.app.screens._5settings.BlockedUsers
import com.spint.app.screens._5settings.BugExplanationScreen
import com.spint.app.screens._5settings.BugsAndSuggestion
import com.spint.app.screens._5settings.ClearSearchHistory
import com.spint.app.screens._5settings.DeleteAccount
import com.spint.app.screens._5settings.EditName
import com.spint.app.screens._5settings.EditPassword
import com.spint.app.screens._5settings.EditPhoneNumber
import com.spint.app.screens._5settings.EditUserName
import com.spint.app.screens._5settings.FeaturesListScreen
import com.spint.app.screens._5settings.HelpCentre
import com.spint.app.screens._5settings.Logout
import com.spint.app.screens._5settings.MyData
import com.spint.app.screens._5settings.PermissionsUI
import com.spint.app.screens._5settings.SafetyAndPrivacy
import com.spint.app.screens._5settings.SavedLoginInfo
import com.spint.app.screens.auth.login.LoginScreenWrapperNewUI
import com.spint.app.screens.auth.createAccount.SignupScreenNewUI
import com.spint.app.screens.common.CameraXScreen
import com.spint.app.screens.common.ImagePreviewScreen
import com.spint.app.screens.onboarding.screen.WelcomeScreen
import com.spint.app.screens.pings.EditScreen
import com.spint.app.screens.pings.templates.PingTemplateSelector
import com.spint.app.screens.pings.templates.visualPingTemplates
import com.spint.app.viewmodels.ImageUploadViewModel
import com.spint.app.viewmodels.NotificationViewModel
import com.spint.app.viewmodels.SettingsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.spint.app.Duel.presentation.view.DuelScreen2
import com.spint.app.screens._2Events.events.templates.xhmaslive.XHamsLiveScreenWrapper
import com.spint.app.screens._4profile.userPings.MyFlashPostDetailsScreen
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun Navigation(authViewModel: AuthViewModel, screen: String) {
    val navController:NavHostController= rememberNavController();
    val imageUploadViewModel= hiltViewModel<ImageUploadViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val homeViewModel= hiltViewModel<HomeViewModel>()
    val chatViewModel= hiltViewModel<ChatViewModel>()
    val notificationViewModel= hiltViewModel<NotificationViewModel>()
    var token by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        token= Firebase.messaging.token.await()
    }





    NavHost(navController = navController, startDestination =screen){

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
                birthDay=authViewModel.birthDay.value,
                onBirthDayChange={authViewModel.birthDay.value=it},
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
           ChooseUserNameAndCreateAccountScreen(
               authViewModel=authViewModel,
               navController = navController,
               userName = authViewModel.userName.value,
               onUserNameChange = {authViewModel.userName.value=it},
               email = authViewModel.email.value,
               onEmailChange = {authViewModel.email.value=it},
               otp = authViewModel.otp.value,
               onOtpChange = {authViewModel.otp.value=it},
               onGetOtpClicked = {authViewModel.getEmailOtp()},
               onVerifyOtpClicked = {authViewModel.verifyOtp()},
               onSignUpClicked = {authViewModel.registerUser(RegisterUserModel(
                   name=authViewModel.name.value,
                   userName =authViewModel.userName.value,
                   email=authViewModel.email.value,
                   password =authViewModel.password.value,
                   token = token,
                   address =authViewModel.address.value,

               ))}
           )
        }
        composable(SCREENS.FINALUSERCREATION.route){
            FinalUserCreation(authViewModel,navController)
        }
        composable(SCREENS.HOME.route){
            HomeScreenUI( navController,homeViewModel,imageUploadViewModel,authViewModel,chatViewModel)
        }
        composable(SCREENS.PROFILE.route){
           // ProfileScreenUI(navController,imageUploadViewModel,authViewModel)
            ProfileScreenNew(navController,authViewModel, homeViewModel, imageUploadViewModel)
        }
        composable(SCREENS.MY_FLASH_POST_DETAILS_SCREEN.route, arguments = listOf(
            navArgument("id"){type= NavType.StringType}
        )){navBackStackEntry->
            val id=navBackStackEntry.arguments?.getString("id") ?: ""

            MyFlashPostDetailsScreen(navController,id,homeViewModel)
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
              val userName=navBackStackEntry.arguments?.getString("userName")?: ""
              val chatListUserId=navBackStackEntry.arguments?.getString("chatListUserId") ?: ""
              val imagePath = URLDecoder.decode(
                  navBackStackEntry.arguments?.getString("profileImage") ?: "",
                  StandardCharsets.UTF_8.toString()
              )

              SingleChatScreenUI(userName,chatListUserId, imagePath,navController ,chatViewModel)
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
        composable(SCREENS.FLASH_POSTS.route){
            //PingsScreenUI(navController,eventsViewModel)
            FlashPostsScreen(navController,homeViewModel)
        }
        composable(SCREENS.EVENTS_SCREEN.route){
            EventsScreenWrapper(homeViewModel = homeViewModel, navController = navController) {
                homeViewModel.getAllEvents()
            }

        }
        composable(SCREENS.XHAM_LIVE_SCREEN.route){
            XHamsLiveScreenWrapper(homeViewModel = homeViewModel, navController = navController){homeViewModel.getAllEvents()}
        }


        composable(SCREENS.PAST_OFFERS.route){
            PastRaisedOffer(authViewModel = authViewModel, homeViewModel =homeViewModel , navController = navController)
        }
        composable(SCREENS.TABVIEW.route){
            //todo for testing purpose
            TabView(navController,imageUploadViewModel,authViewModel)
        }
        composable(SCREENS.CREATE_PING.route) {
          //  CreateEvent(eventsViewModel, navController)
            //CreatePing(authViewModel, eventsViewModel, navController)
           // CreatePingMainScreen(navController,eventsViewModel)
            CreatePingWrapper(navController,homeViewModel)
        }
        composable(SCREENS.CREATE_EVENT.route, arguments = listOf(navArgument("parentEventId"){
            type= NavType.StringType
            nullable=true
            defaultValue=null
        })) {
            val parentEventId=it.arguments?.getString("parentEventId")
            CreateEventMainScreen(parentEventId,navController,homeViewModel)
        }
        composable(route = SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.route, arguments = listOf(
            navArgument(name = "id"){
                type= NavType.StringType
            }
        )){navBackStackEntry->
            val id=navBackStackEntry.arguments?.getString("id") ?: ""
           // PublicEventDetailsScreenWrapper(id, navController,eventsViewModel,authViewModel)
            EventsDetailsVerticalWrapper(id, navController,homeViewModel,authViewModel)
        }
        composable(SCREENS.PRIVATE_PROFILE.route){
            PrivateProfileScreenWrapper(navController = navController)
        }
//        composable(SCREENS.TIKTOK.route){
//            val list= listOf<String>("1","2","3","4","5","6")
//            Tiktok(videos = list)
//        }
        // when the dropped profile is clicked then it is shown
        composable(route=SCREENS.DROP_PROFILE_USER_PROFILE.route, arguments = listOf(navArgument("dropProfileResponse"){ type= NavType.StringType })){navBackStackEntry ->
            val json=navBackStackEntry.arguments?.getString("dropProfileResponse")
            val dropProfileResponse=json?.let { Json.decodeFromString<DropProfileResponse>(it) }
            DropProfileUserProfile(navController,chatViewModel, homeViewModel ,dropProfileResponse)

        }
        composable(route=SCREENS.PING_DETAILS.route, arguments = listOf(navArgument("flashPostResponse"){ type= NavType.StringType })){navBackStackEntry ->
            val json=navBackStackEntry.arguments?.getString("flashPostResponse")
            val flashPostResponse=json?.let { Json.decodeFromString<FlashPostResponse>(it) }
            FlashPostDetailsScreen(navController,flashPostResponse,homeViewModel)
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
                homeViewModel=homeViewModel,
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
                            homeViewModel.showDropDialog.value=true
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
            UserPublicProfile(homeViewModel,navController,userId)

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


        composable(
            route = SCREENS.COMMENT.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val postId = navBackStackEntry.arguments?.getString("postId") ?: ""
            FlashPostCommentScreen(postId,homeViewModel)
        }
        composable(SCREENS.DUEL.route){
            DuelScreen2(navController)
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