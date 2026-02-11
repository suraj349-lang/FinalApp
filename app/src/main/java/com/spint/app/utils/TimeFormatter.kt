package com.spint.app.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
fun convertToIST(utcTimestamp: String): String {
    val formats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'"
    )

    var date: Date? = null
    for (pattern in formats) {
        try {
            val sdf = SimpleDateFormat(pattern)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            date = sdf.parse(utcTimestamp)
            if (date != null) break
        } catch (_: Exception) {}
    }

    if (date == null) return "Invalid time"

    val istFormat = SimpleDateFormat("hh:mm")
    istFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    return istFormat.format(date)
}

