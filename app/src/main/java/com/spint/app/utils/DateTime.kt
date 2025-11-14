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
    try {
        // Parse the ISO string to a Date object
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val zonedDateTime = sdf.parse(isoString) ?: return "Invalid"

        // Get the current date
        val now = Date()

        // Set up formatters for time and date
        val timeFormatter = SimpleDateFormat("HH:mm")
        val dateFormatter = SimpleDateFormat("dd MMM")

        // Extract date (day, month, year) from both `zonedDateTime` and `now`
        val calendarZoned = Calendar.getInstance()
        calendarZoned.time = zonedDateTime
        val calendarNow = Calendar.getInstance()
        calendarNow.time = now

        // Compare dates (day, month, year)
        return when {
            calendarZoned.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR) &&
                    calendarZoned.get(Calendar.DAY_OF_YEAR) == calendarNow.get(Calendar.DAY_OF_YEAR) -> {
                // Same day
                "Today, ${timeFormatter.format(zonedDateTime)}"
            }
            calendarZoned.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR) &&
                    calendarZoned.get(Calendar.DAY_OF_YEAR) == calendarNow.get(Calendar.DAY_OF_YEAR) - 1 -> {
                // Yesterday
                "Yesterday, ${timeFormatter.format(zonedDateTime)}"
            }
            else -> {
                // Different day
                "${dateFormatter.format(zonedDateTime)}, ${timeFormatter.format(zonedDateTime)}"
            }
        }
    } catch (e: Exception) {
        Log.e("FormatDateTime", "Error formatting date: ${e.printStackTrace()}", e)
        return "Error getting time"
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


@SuppressLint("SimpleDateFormat")
fun getFormattedTimeAndFlag(isoString: String): Pair<String, Boolean> {
    return try {
        // Parse ISO 8601 timestamp like "2025-11-12T15:30:00Z"
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val targetDate = sdf.parse(isoString)
        val now = Date()

        if (targetDate == null) return Pair("Invalid", true)

        if (targetDate.before(now)) {
            return Pair("Expired", true)
        }

        // Calculate difference in milliseconds
        val diffMillis = targetDate.time - now.time

        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30 // simple approximation for long durations

        return when {
            months >= 1 -> {
                val remainingDays = days % 30
                Pair("${months}mo ${remainingDays}d", false)
            }
            days >= 1 -> Pair("$days day${if (days > 1) "s" else ""}", false)
            hours >= 1 -> {
                val remMinutes = minutes % 60
                Pair("${hours}h ${remMinutes}m", false)
            }
            minutes >= 1 -> {
                val remSeconds = seconds % 60
                Pair("${minutes}m ${remSeconds}s", true)
            }
            else -> Pair("${seconds}s", true)
        }
    } catch (e: Exception) {
        Pair("Invalid", true)
    }
}
