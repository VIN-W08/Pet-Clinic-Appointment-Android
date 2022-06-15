package com.example.vin.petclinicappointment.ui.components.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Composable
fun LabelScheduleInput(
    label: String,
    dateValue: String,
    timeValue: String,
    onDateValueChange: (date: LocalDate) -> Unit,
    onTimeValueChange: (time: LocalTime) -> Unit,
    minDate: Long? = null,
    maxDate: Long? = null,
    required: Boolean = true,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val currentDateTime = LocalDateTime.now()
    val currentDayOfMonth = currentDateTime.dayOfMonth
    val currentMonth = currentDateTime.month.value - 1
    val currentYear = currentDateTime.year

    val currentHour = currentDateTime.hour
    val currentMinute = currentDateTime.minute

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onDateValueChange(LocalDate.of(year, month + 1, dayOfMonth))
        }, currentYear, currentMonth, currentDayOfMonth
    )
    if (minDate !== null) {
        datePickerDialog.datePicker.minDate = minDate
    }
    if (maxDate !== null) {
        datePickerDialog.datePicker.maxDate = maxDate
    }

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            onTimeValueChange(LocalTime.of(hour, minute))
        },
        currentHour, currentMinute, true
    )

    Row (
        modifier = modifier
            .padding(
                bottom = PetClinicAppointmentTheme.dimensions.grid_4
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = PetClinicAppointmentTheme.dimensions.grid_2)
                .weight(0.4f)
        ) {
            Text(
                label,
                style = PetClinicAppointmentTheme.typography.h3
            )
            if(required){
                Text(
                    "*",
                    style = PetClinicAppointmentTheme.typography.h3,
                    color = Color.Red
                )
            }
        }
        Row(
            Modifier
                .padding(end = PetClinicAppointmentTheme.dimensions.grid_2)
                .weight(0.6f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextInput(
                value = dateValue,
                onValueChange = {},
                trailingIcon = {
                    IconButton(
                        icon = Icons.Rounded.CalendarToday,
                        contentDescription = "calendar",
                        onClick = {
                            if(!datePickerDialog.isShowing) {
                                datePickerDialog.show()
                            }
                        }
                    )
                },
                onFocus = {
                    if(!datePickerDialog.isShowing) {
                        datePickerDialog.show()
                    }
                },
                readOnly = true,
                containerModifier = Modifier
                    .weight(0.8f),
                modifier = Modifier.fillMaxWidth()
            )
            Divider(Modifier.width(PetClinicAppointmentTheme.dimensions.grid_1))
            TextInput(
                value = timeValue,
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
                readOnly = true,
                containerModifier = Modifier.weight(0.55f),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}