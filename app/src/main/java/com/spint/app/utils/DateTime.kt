package com.spint.app.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(isoString: String): String {
    try {
    val zonedDateTime = ZonedDateTime.parse(isoString)
    val now = ZonedDateTime.now(ZoneId.systemDefault())

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")

    return when {
        zonedDateTime.toLocalDate().isEqual(now.toLocalDate()) ->
            "Today, ${zonedDateTime.format(timeFormatter)}"

        zonedDateTime.toLocalDate().isEqual(now.minusDays(1).toLocalDate()) ->
            "Yesterday, ${zonedDateTime.format(timeFormatter)}"

        else -> "${zonedDateTime.format(dateFormatter)}, ${zonedDateTime.format(timeFormatter)}"
    }}catch (e:Exception){
        Log.e("FormatDateTime", "formatDateTime:${e.printStackTrace()} ",e)
        return "Error getting time"
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun Long.toRelativeTime(): String {
    val now = Instant.now()
    val eventTime = Instant.ofEpochMilli(this)
    val zoneId = ZoneId.systemDefault()

    val duration = Duration.between(eventTime, now)
    val minutesAgo = duration.toMinutes()
    val hoursAgo = duration.toHours()
    val daysAgo = duration.toDays()

    return when {
        minutesAgo < 1 -> "Just now"
        minutesAgo < 60 -> "$minutesAgo min ago"
        hoursAgo < 24 -> "$hoursAgo hour${if (hoursAgo != 1L) "s" else ""} ago"
        daysAgo == 0L -> "Today"
        daysAgo == 1L -> "Yesterday"
        daysAgo < 7 -> "$daysAgo days ago"
        else -> {
            val dateTime = eventTime.atZone(zoneId).toLocalDateTime()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")
            dateTime.format(formatter)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedTimeAndFlag(isoString: String): Pair<String, Boolean> {
    return try {
        val targetTime = Instant.parse(isoString)
        val now = Instant.now()

        if (targetTime.isBefore(now)) {
            return Pair("Expired", true)
        }

        val duration = Duration.between(now, targetTime)
        val days = duration.toDays()

        when {
            days >= 30 -> {
                val startDate = LocalDate.now(ZoneId.systemDefault())
                val endDate = targetTime.atZone(ZoneId.systemDefault()).toLocalDate()
                val period = Period.between(startDate, endDate)

                val months = period.months
                val remainingDays = period.days
                Pair(
                    if (months > 0) "${months}mo ${remainingDays}d" else "${remainingDays}d",
                    false
                )
            }
            days >= 1 -> Pair("$days day${if (days > 1) "s" else ""}", false)
            duration.toHours() >= 1 -> {
                val hours = duration.toHours()
                val minutes = duration.toMinutes() % 60
                Pair("${hours}h ${minutes}m", false)
            }
            duration.toMinutes() >= 1 -> {
                val minutes = duration.toMinutes()
                val seconds = duration.seconds % 60
                Pair("${minutes}m ${seconds}s", true) // less than an hour
            }
            else -> Pair("${duration.seconds}s", true) // less than an hour
        }
    } catch (e: DateTimeParseException) {
        Pair("Invalid", true)
    }
}
