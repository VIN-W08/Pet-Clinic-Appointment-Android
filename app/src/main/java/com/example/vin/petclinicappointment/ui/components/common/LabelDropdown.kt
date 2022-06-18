package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun LabelDropdown(
    label: String,
    option: DropdownOption,
    onValueChange: (value: String) -> Unit,
    optionList: List<DropdownOption>,
    onClickOption: (option: DropdownOption) -> Unit,
    required: Boolean = true,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            Modifier.weight(0.4f)
        ) {
            Text(
                label,
                style = PetClinicAppointmentTheme.typography.h3
            )
            if (required) {
                Text(
                    "*",
                    style = PetClinicAppointmentTheme.typography.h3,
                    color = Color.Red
                )
            }
        }
        Row(
            Modifier.weight(0.6f)
        ) {
            DropdownTextInput(
                option = option,
                onValueChange = onValueChange,
                optionList = optionList,
                onClickOption = onClickOption,
                readOnly = true
            )
        }
    }
}