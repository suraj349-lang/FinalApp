package com.example.finalapp.utils.constants

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.finalapp.R

object Constants {
    const val IP_ADD ="3.108.55.84" //"3.108.55.84"//""//"192.168.29.95" //  3.108.55.84
    const val SOCKET_CHAT_BASE_URL = "http://${IP_ADD}/chat/"
    const val TEMP_SOCKET_URL = "http://${IP_ADD}/";
    const val BASE_URL = "http://${IP_ADD}/"
    const val APP_NAME="Spint"
    val APP_ICON = R.drawable.app_icon_dynamic
    const val TAG = APP_NAME
    val FONT_EXTRA_LIGHT = FontFamily(Font(R.font.k2d_extralight));
    val FONT_LIGHT = FontFamily(Font(R.font.k2d_light));
    val FONT_MEDIUM = FontFamily(Font(R.font.k2d_medium));
    val DONGLE_BOLD =FontFamily(Font(R.font.dongle_bold));
    val DONGLE_NORMAL=FontFamily(Font(R.font.dongle_regular));
    val DONGLE_LIGHT=FontFamily(Font(R.font.dongle_light));
    val LOGIN_BACKGROUND= Color.DarkGray
    val LOGIN_SURFACE=Color(0xFFFFFFFF)

    val APP_NAME_FONT= FontFamily(Font(R.font.alfaslabone))
}
