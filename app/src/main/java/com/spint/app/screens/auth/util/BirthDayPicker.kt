package com.spint.app.screens.auth.util

import android.app.Activity
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.spint.app.R
import com.spint.app.utils.constants.Constants
import java.util.Calendar
@Composable
fun BirthdayPicker(
    birthDay: String,
    onBirthDayChange: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val calendar = remember { Calendar.getInstance() }

    val datePickerDialog = remember {
        DatePickerDialog(
            activity,
            { _, year, month, dayOfMonth ->
                val formattedDate =
                    "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                onBirthDayChange(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() }
    ) {
        OutlinedTextField(
            value = birthDay,
            onValueChange = {},
            enabled = false, // 🔥 IMPORTANT
            placeholder = {
                Text(
                    text = "Birthday",
                    fontFamily = Constants.FONT_LIGHT
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontFamily = Constants.FONT_MEDIUM
            ),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledPlaceholderColor = Color.LightGray
            ),
            trailingIcon = {
                Image(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = "",modifier=Modifier.size(24.dp)
                )
            }
        )
    }
}
