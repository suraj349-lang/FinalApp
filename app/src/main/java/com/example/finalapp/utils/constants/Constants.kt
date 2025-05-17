package com.example.finalapp.utils.constants

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.finalapp.R

object Constants {
    const val IP_ADD = "192.168.1.11"//""//"192.168.29.95"
    const val SOCKET_CHAT_BASE_URL = "http://${IP_ADD}:80/chat/"
    const val TEMP_SOCKET_URL = "http://${IP_ADD}:5001/";
    const val BASE_URL = "http://${IP_ADD}:5000/"
    const val APP_NAME="Spint"
    val APP_ICON = R.drawable.app_icon_dynamic
    const val TAG = APP_NAME
    val FONT_EXTRA_LIGHT = FontFamily(Font(R.font.k2d_extralight));
    val FONT_LIGHT = FontFamily(Font(R.font.k2d_light));
    val FONT_MEDIUM = FontFamily(Font(R.font.k2d_medium));
    val DONGLE_BOLD =FontFamily(Font(R.font.dongle_bold));
    val DONGLE_NORMAL=FontFamily(Font(R.font.dongle_regular));
    val DONGLE_LIGHT=FontFamily(Font(R.font.dongle_light));

    val APP_NAME_FONT= FontFamily(Font(R.font.alfaslabone))
}
