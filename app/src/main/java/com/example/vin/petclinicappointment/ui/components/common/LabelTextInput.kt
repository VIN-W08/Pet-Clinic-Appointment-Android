package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
){
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = PetClinicAppointmentTheme.typography.h3.fontSize
            )
        ) {
            append(label)
        }
        if(required) {
            withStyle(
                style = SpanStyle(
                    color = PetClinicAppointmentTheme.colors.error,
                    fontWeight = PetClinicAppointmentTheme.typography.h3.fontWeight,
                    fontSize = PetClinicAppointmentTheme.typography.h3.fontSize
                )
            ) {
                append("*")
            }
        }
    }

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
            Text(annotatedString)
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