package com.spint.app.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@SuppressLint("SimpleDateFormat")
fun formatDateTime(isoString: String): String {
    return try {
        // Remove fractional seconds if present
        val cleaned = isoString.replace(Regex("\\.\\d+"), "")

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val zonedDateTime = sdf.parse(cleaned) ?: return "Invalid"

        // Get current date in UTC
        val now = Date()

        val timeFormatter = SimpleDateFormat("HH:mm").apply {
            timeZone = TimeZone.getTimeZone("UTC")  // <- important
        }
        val dateFormatter = SimpleDateFormat("dd MMM").apply {
            timeZone = TimeZone.getTimeZone("UTC")  // <- important
        }

        val calendarZoned = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { time = zonedDateTime }
        val calendarNow = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { time = now }

        return when {
            calendarZoned.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR) &&
                    calendarZoned.get(Calendar.DAY_OF_YEAR) == calendarNow.get(Calendar.DAY_OF_YEAR) ->
                "Today, ${timeFormatter.format(zonedDateTime)}"

            calendarZoned.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR) &&
                    calendarZoned.get(Calendar.DAY_OF_YEAR) == calendarNow.get(Calendar.DAY_OF_YEAR) - 1 ->
                "Yesterday, ${timeFormatter.format(zonedDateTime)}"

            else -> "${dateFormatter.format(zonedDateTime)}, ${timeFormatter.format(zonedDateTime)}"
        }

    } catch (e: Exception) {
        Log.e("FormatDateTime", "Error formatting date", e)
        "Error getting time"
    }
}


@SuppressLint("SimpleDateFormat")
fun Long.toRelativeTime(): String {
    val now = Date()
    val eventTime = Date(this)

    val durationMillis = now.time - eventTime.time
    val secondsAgo = durationMillis / 1000
    val minutesAgo = secondsAgo / 60
    val hoursAgo = minutesAgo / 60
    val daysAgo = hoursAgo / 24

    return when {
        minutesAgo < 1 -> "Just now"
        minutesAgo < 60 -> "$minutesAgo min ago"
        hoursAgo < 24 -> "$hoursAgo hour${if (hoursAgo != 1L) "s" else ""} ago"
        daysAgo == 0L -> "Today"
        daysAgo == 1L -> "Yesterday"
        daysAgo < 7 -> "$daysAgo days ago"
        else -> {
            val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm a")
            formatter.format(eventTime)
        }
    }
}


fun getFormattedTimeAndFlag(isoString: String): Triple<String, Boolean, Boolean> {
    return try {
        val cleaned = isoString.replace(Regex("\\.\\d+"), "")

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val targetDate = sdf.parse(cleaned) ?: return Triple("Invalid", true,false)
        val now = Date()

        if (targetDate.before(now)) return Triple("00-00", true,true)

        val diffMillis = targetDate.time - now.time
        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30

        when {
            months >= 1 -> Triple("${months}mo ${days % 30}d", false,false)
            days >= 1 -> Triple("$days day${if (days > 1) "s" else ""}", false,false)
            hours >= 1 -> Triple("${hours}h ${minutes % 60}m", false,false)
            minutes >= 1 -> Triple("${minutes}m ${seconds % 60}s", true,false)
            else -> Triple("${seconds}s", true,false)
        }

    } catch (e: Exception) {
        Log.e("getFormattedTimeAndFlag", "getFormattedTimeAndFlag: $e",e.fillInStackTrace() )
        Triple("Invalid", true,false)
    }
}

