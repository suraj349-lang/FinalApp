package com.example.finalapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(isoString: String): String {
    val zonedDateTime = ZonedDateTime.parse(isoString) //.withZoneSameInstant(ZoneId.systemDefault())
    val now = ZonedDateTime.now(ZoneId.systemDefault())

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")

    return when {
        zonedDateTime.toLocalDate().isEqual(now.toLocalDate()) ->
            "Today, ${zonedDateTime.format(timeFormatter)}"

        zonedDateTime.toLocalDate().isEqual(now.minusDays(1).toLocalDate()) ->
            "Yesterday, ${zonedDateTime.format(timeFormatter)}"

        else -> "${zonedDateTime.format(dateFormatter)}, ${zonedDateTime.format(timeFormatter)}"
    }
}
