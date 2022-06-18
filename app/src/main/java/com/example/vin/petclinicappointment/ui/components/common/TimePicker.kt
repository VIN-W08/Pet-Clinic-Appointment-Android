package com.example.vin.petclinicappointment.ui.components.common

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun TimePicker(
    value: String,
    onTimeValueChange: (time: LocalTime) -> Unit
){
    val context = LocalContext.current
    val currentTime = if(value.isNullOrEmpty()) LocalTime.now() else LocalTime.parse(value)
    val currentHour = currentTime.hour
    val currentMinute = currentTime.minute

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            onTimeValueChange(LocalTime.of(hour, minute))
        },
        currentHour, currentMinute, true
    )
    TextInput(
        value = value,
        onValueChange = {},
        trailingIcon = {
            IconButton(
                icon = Icons.Rounded.Schedule,
                contentDescription = "time",
                onClick = {
                    if(!timePickerDialog.isShowing) {
                        timePickerDialog.show()
                    }
                }
            )
        },
        onFocus = {
            if(!timePickerDialog.isShowing) {
                timePickerDialog.show()
            }
        },
        readOnly = true
    )
}