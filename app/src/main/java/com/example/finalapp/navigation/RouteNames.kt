package com.example.finalapp.navigation

import android.net.Uri
import com.example.finalapp.model.DropProfileResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


sealed class SCREENS(val route:String){
    object SPLASH:SCREENS("splash_Screen")
    object LOGIN:SCREENS("login_Screen")
    object SIGNUP:SCREENS("signup_screen")
    object OTP:SCREENS("enter-otp")
    object  OTP2:SCREENS("otp")
    object FINALUSERCREATION:SCREENS("final_user_creation")


    object HOME:SCREENS("home_screen")
    object PROFILE:SCREENS("profile_screen")
    object SETTINGS:SCREENS("settings_screen")
    object PINGS:SCREENS("personal")
    object NOTIFICATIONS:SCREENS("notifications_screen")
    object CHAT_LIST:SCREENS("chat_screen")
    object SINGLE_CHAT:SCREENS("singleChat/{userName}/{chatListUserId}"){
        fun createPath(userName:String,chatListUserId:String):String{
            return "singleChat/${userName}/${chatListUserId}"
        }
    }

    object GALLERY:SCREENS("gallery_picker")
    object WELCOME:SCREENS("welcome")
    object ALL_USERS:SCREENS("all_users")

    object DROP_PROFILE:SCREENS("drop_profile")
    object CREATE_PING:SCREENS("create_ping")
    object CREATE_EVENT:SCREENS("create_event_public?parentEventId={parentEventId}"){
        fun createRoute(parentEventId:String?):String {
            return if(parentEventId.isNullOrEmpty()){
                "create_event_public"
            }else{
                "create_event_public?parentEventId=$parentEventId"
            }
        }
    }
    object PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER:SCREENS("publicEventDetailsScreenWrapper/{id}"){
        fun createRoute(id: String):String{
            return "publicEventDetailsScreenWrapper/$id"
        }
    }
    object PRIVATE_PROFILE:SCREENS("private_profile")
    object PAST_OFFERS:SCREENS("past_offers")
    object TABVIEW:SCREENS("tab_view")
    object TIKTOK:SCREENS("tiktok")
    object PREMIUM_CREATE_EVENT:SCREENS("premium")
    object DROP_PROFILE_USER_PROFILE:SCREENS("drop_profile_user_profile/{dropProfileResponse}"){
        fun createRoute(dropProfileResponse: DropProfileResponse):String{
            val profileJson= Uri.encode(Json.encodeToString(dropProfileResponse))
            return "drop_profile_user_profile/$profileJson"
        }
    }
    object USER_PUBLIC_PROFILE:SCREENS("user_public_profile/{userId}"){
        fun createPath(userId: String):String{
            return "user_public_profile/$userId"
        }
    }

    object COMMENT:SCREENS("comment")
    object IMAGE_CROPPER:SCREENS("auto_image_cropper")

    object CAMERAX_SCREEN:SCREENS("camerax_screen")





    //-----------------------------------------------SETTINGS-------------------------------------------------------------------------------------------

    object EDIT_NAME:SCREENS("edit_name")
    object EDIT_USER_NAME:SCREENS("edit_user_name")
    object PHONE_NUMBER:SCREENS("phone_number")
    object PASSWORD:SCREENS("password")
    object DELETE_ACCOUNT:SCREENS("delete_account")

    //--------------------------------------
    object BUGS_AND_SUGGESTION:SCREENS("bugs")
    object FEATURES_SCREEN:SCREENS("features_list_screen")
    object BUG_EXPLANATION_SCREEN:SCREENS("bug_explanation_screen")
    object SAFETY_AND_PRIVACY:SCREENS("safety")
    object HELP_CENTRE:SCREENS("help_centre")
    //--------------------------------------
    object PRIVACY_POLICY:SCREENS("privacy_policy")
    object SAFETY_CENTRE:SCREENS("safety_centre")
    object TERMS_OF_SERVICE:SCREENS("terms_of_service")
    object OTHER_LEGAL:SCREENS("other_legal")

    //-------------------------------------
    object CLEAR_SEARCH_HISTORY:SCREENS("clear_search_history")
    object PERMISSIONS:SCREENS("permissions")
    object BLOCKED_USERS:SCREENS("blocked_users")
    object SAVED_LOGIN_INFO:SCREENS("saved_login_info")
    object MY_DATA:SCREENS("my_data")
    object LOG_OUT:SCREENS("log_out")



}