package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun TextInput(
    containerModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    maxLines: Int = 1,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    hasOutline: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    inputType: String = "text",
    showError: Boolean = false,
    errorMessage: String? = null,
    onFocus: (() -> Unit)? = null,
    onLeaveFocus: (() -> Unit)? = null,
){
    var isFocused by rememberSaveable{ mutableStateOf(false) }
    var initialInput by rememberSaveable { mutableStateOf( true )}

    Column (
        modifier = containerModifier
    ){
        if(hasOutline){
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                        if (isFocused) {
                            initialInput = false
                            if (onFocus != null) {
                                onFocus()
                            }
                        } else {
                            if (onLeaveFocus != null) {
                                onLeaveFocus()
                            }
                        }
                    },
                placeholder = { Text(placeholder) },
                maxLines = maxLines,
                textStyle = MaterialTheme.typography.body1,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                visualTransformation = if (inputType === "password") PasswordVisualTransformation() else visualTransformation,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )
        } else {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                        if (isFocused) {
                            initialInput = false
                            if (onFocus != null) {
                                onFocus()
                            }
                        } else {
                            if (onLeaveFocus != null) {
                                onLeaveFocus()
                            }
                        }
                    },
                placeholder = { Text(placeholder) },
                maxLines = maxLines,
                textStyle = MaterialTheme.typography.body1,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                visualTransformation = if (inputType === "password") PasswordVisualTransformation() else visualTransformation,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )
        }
        if(errorMessage !== null && showError && !initialInput) {
            Column {
                Text(
                    errorMessage,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}