package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun LabelTextInput(
    label: String,
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean = true,
    numberOnly: Boolean = false,
    numberWithDotAndHyphenOnly: Boolean = false,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions
){
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(0.4f),
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
                .weight(0.6f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextInput(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly,
                numberOnly = numberOnly,
                numberWithDotAndHyphenOnly = numberWithDotAndHyphenOnly,
                keyBoardOptions = keyboardOptions,
                containerModifier = Modifier.fillMaxWidth(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}