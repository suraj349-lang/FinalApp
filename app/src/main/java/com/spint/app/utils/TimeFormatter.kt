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
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
fun convertToIST(utcTimestamp: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdf.parse(utcTimestamp)

    val istFormat = SimpleDateFormat("hh:mm")
    istFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    return istFormat.format(date)
}

//fun calculateExpirationIso(hoursToAdd: Int): String {
//    val expirationInstant = Instant.now().plusSeconds(hoursToAdd * 3600L)
//    return DateTimeFormatter.ISO_INSTANT
//        .withZone(ZoneOffset.UTC)
//        .format(expirationInstant)
//}