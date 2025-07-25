package com.example.finalapp.screens._5settings.legal

import android.content.Context


fun readRawTextFile(context: Context, resId: Int): String {
    return context.resources.openRawResource(resId)
        .bufferedReader()
        .use { it.readText() }
}
