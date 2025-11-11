package com.spint.app.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun convertToIST(utcTimestamp: String):String// Pair<String, String>
{
    val instant = Instant.parse(utcTimestamp)

    val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Kolkata"))

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm")

   // val date = zonedDateTime.format(dateFormatter)
    val time = zonedDateTime.format(timeFormatter)

   // return Pair(date, time)
    return  time;
}
