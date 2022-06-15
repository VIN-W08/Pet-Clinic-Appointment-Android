package com.example.vin.petclinicappointment.ui.components.common

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextInput(
    containerModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    maxLines: Int = 1,
    singleLine: Boolean = true,
    shape: Shape = RectangleShape,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    hasOutline: Boolean = true,
    outlineColor: Color = Color.Gray,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    inputType: String = "text",
    readOnly: Boolean = false,
    keyBoardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyBoardActions: KeyboardActions = KeyboardActions.Default,
    showError: Boolean = false,
    errorMessage: String? = null,
    onFocus: (() -> Unit)? = null,
    onLeaveFocus: (() -> Unit)? = null
){
    var isFocused by rememberSaveable{ mutableStateOf(false) }
    var initialInput by rememberSaveable { mutableStateOf( true )}
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    val keyModifier =
        if(hasOutline)
            modifier
                .border(
                    width = PetClinicAppointmentTheme.dimensions.grid_0_125,
                    color = if (isFocused) PetClinicAppointmentTheme.colors.primary else outlineColor,
                    shape = shape
                )
                .padding(
                    start = PetClinicAppointmentTheme.dimensions.grid_1_5,
                    end = PetClinicAppointmentTheme.dimensions.grid_1_5
                )
        else modifier

    Column (
        containerModifier
            .width(PetClinicAppointmentTheme.dimensions.grid_8 * 4)
            .clip(shape)
    ){
        Box(
            keyModifier
                .width(PetClinicAppointmentTheme.dimensions.grid_8 * 4)
                .height(PetClinicAppointmentTheme.dimensions.grid_5)
                .focusRequester(focusRequester)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { focusRequester.requestFocus() }
                )
        ) {
            Row {
                if (leadingIcon !== null) {
                    Row(
                        Modifier
                            .fillMaxHeight()
                            .padding(end = PetClinicAppointmentTheme.dimensions.grid_0_5),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        leadingIcon()
                    }
                }
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement =
                    if (hasOutline) { if(singleLine) Arrangement.Center else Arrangement.Top }
                    else Arrangement.SpaceBetween,
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = {
                            if(inputType == "number") {
                                if ("[^0-9]".toRegex().containsMatchIn(it)) return@BasicTextField
                            }
                            onValueChange(it)
                        },
                        modifier = Modifier
                            .padding(
                                top = if(!singleLine) PetClinicAppointmentTheme.dimensions.grid_1_5
                                else 0.dp
                            )
                            .fillMaxWidth()
                            .onFocusChanged {
                                isFocused = it.isFocused
                                if (isFocused) {
                                    initialInput = false
                                    if (onFocus != null) {
                                        onFocus()
                                    }
                                } else if (!isFocused && !initialInput) {
                                    if (onLeaveFocus != null) {
                                        onLeaveFocus()
                                    }
                                }
                            },
                        decorationBox = {
                            Row {
                                if (value.isEmpty()) {
                                    Text(
                                        text = placeholder,
                                        color = Color.Gray
                                    )
                                }
                            }
                            it()
                        },
                        maxLines = maxLines,
                        singleLine = singleLine,
                        textStyle = MaterialTheme.typography.body1,
                        visualTransformation = if (inputType === "password") PasswordVisualTransformation() else visualTransformation,
                        keyboardOptions = keyBoardOptions,
                        keyboardActions = keyBoardActions,
                        readOnly = readOnly
                    )
                }
                if (trailingIcon !== null) {
                    Row(
                        Modifier
                            .fillMaxHeight()
                            .padding(start = PetClinicAppointmentTheme.dimensions.grid_0_5),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        trailingIcon()
                    }
                }
            }
            if (!hasOutline) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (leadingIcon === null && trailingIcon === null) {
                        Spacer(modifier = Modifier.height(PetClinicAppointmentTheme.dimensions.grid_1))
                    }
                    Divider(
                        Modifier.fillMaxWidth(),
                        color = if (isFocused) PetClinicAppointmentTheme.colors.primary else Color.Gray,
                        thickness = if (isFocused) PetClinicAppointmentTheme.dimensions.grid_0_25 else PetClinicAppointmentTheme.dimensions.grid_0_125,
                    )
                }
            }
        }
        if(errorMessage !== null && showError) {
            Column {
                Text(
                    errorMessage,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}