package com.example.vin.petclinicappointment.ui.components.common

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@Composable
fun DatePicker(
    onDateValueChange: (date: LocalDate) -> Unit,
    minDate: Long? = null,
    maxDate: Long? = null,
    hasBorder: Boolean = false,
    tint: Color = Color.Unspecified,
    containerModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val currentDate = LocalDate.now()
    val currentDayOfMonth = currentDate.dayOfMonth
    val currentMonth = currentDate.month.value - 1
    val currentYear = currentDate.year

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

    IconButton(
        icon = Icons.Rounded.CalendarToday,
        contentDescription = "calendar",
        containerModifier = containerModifier,
        modifier = modifier,
        hasBorder = hasBorder,
        tint = tint
    ) {
        if(!datePickerDialog.isShowing) {
            datePickerDialog.show()
        }
    }
}