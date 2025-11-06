package com.example.finalapp.utils.constants

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.finalapp.R

object Constants {

    const val IP_ADD ="192.168.24.79"//"3.108.55.84"
    const val SOCKET_CHAT_BASE_URL = "http://${IP_ADD}/chat/5001/"
    const val TEMP_SOCKET_URL = "http://${IP_ADD}:5001/"
    const val BASE_URL = "http://${IP_ADD}:5000/"


    const val APP_NAME="Spint"
    val APP_ICON = R.drawable.app_icon_dynamic
    const val TAG = APP_NAME



    val FONT_EXTRA_LIGHT = FontFamily(Font(R.font.k2d_extralight))
    val FONT_LIGHT = FontFamily(Font(R.font.k2d_light))
    val FONT_MEDIUM = FontFamily(Font(R.font.k2d_medium))

    val ROBOTO_CONDENSED = FontFamily(Font(R.font.roboto_condensed));

    val DONGLE_BOLD =FontFamily(Font(R.font.dongle_bold));
    val DONGLE_NORMAL=FontFamily(Font(R.font.dongle_regular));
    val DONGLE_LIGHT=FontFamily(Font(R.font.dongle_light));
    val LOGIN_BACKGROUND= Color.DarkGray
    val LOGIN_SURFACE=Color(0xFFFFFFFF)

    val USER_NAME_FONT= FontFamily(Font(R.font.inter))

    val APP_NAME_FONT= FontFamily(Font(R.font.alfaslabone))

    //splash screen
    val SPLASH_SCREEN_COLOR= Color(0xFFA32902)

    // home screen---------------------------------

    val HOME_TOP_BAR_TITLE_COLOR= Color(0xFF0682F1)
    val HOME_TOP_BAR_COLOR= Color(0xFF1A1919)//0xFF260235
    val HOME_TOP_BAR_ICON_COLOR= Color.White

    val HOME_STATUS_BAR_COLOR= HOME_TOP_BAR_COLOR
    val HOME_NAV_BAR_COLOR= HOME_TOP_BAR_COLOR

    val TAB_ROW_BACKGROUND_COLOR=HOME_TOP_BAR_COLOR
    val TAB_ROW_ACTIVE_TEXT_COLOR=Color.White
    val TAB_ROW_INACTIVE_COLOR= Color.LightGray
    val TAB_ROW_INDICATOR_COLOR=Color.White

    val HOME_BOTTOM_BAR_COLOR= Color(0xFF1A1919)
    val BOTTOM_BAR_ACTIVE_TEXT_COLOR= Color.White

    val BOTTOM_BAR_INACTIVE_ICON_COLOR= Color.LightGray
    val BOTTOM_BAR_INACTIVE_TEXT_COLOR= Color.LightGray
}
