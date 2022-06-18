package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.PopupProperties
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

data class DropdownOption (
    val id: String,
    val value: String
        )

@Composable
fun DropdownTextInput(
    option: DropdownOption,
    onValueChange: (value: String) -> Unit,
    placeholder: String = "",
    optionList: List<DropdownOption>,
    onClickOption: (option: DropdownOption) -> Unit,
    dismissEnabled: Boolean = true,
    autoCollapseMenuEnabled: Boolean = true,
    onCancel: (() -> Unit)? = null,
    showEmptyListError: Boolean = true,
    readOnly: Boolean = false,
    containerModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
){
    var expandedDropdown by rememberSaveable { mutableStateOf(false) }
    var dropdownTextInputWidth by rememberSaveable { mutableStateOf(0) }
    var dropdownTextInputHeight by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = containerModifier
            .width(PetClinicAppointmentTheme.dimensions.grid_8 * 4)
            .height(PetClinicAppointmentTheme.dimensions.grid_5)
            .onSizeChanged { size ->
                dropdownTextInputWidth = size.width
                dropdownTextInputHeight = size.height
            },
    ) {
        TextInput(
            value = option.value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            readOnly = readOnly,
            containerModifier = Modifier
                .width(with(LocalDensity.current) { dropdownTextInputWidth.toDp() })
                .height(with(LocalDensity.current) { dropdownTextInputHeight.toDp() }),
            modifier = modifier
                .width(with(LocalDensity.current) { dropdownTextInputWidth.toDp() })
                .height(with(LocalDensity.current) { dropdownTextInputHeight.toDp() }),
            trailingIcon = {
                if (option.value.isNotEmpty() && onCancel !== null) {
                    IconButton(
                        icon = Icons.Rounded.Cancel,
                        contentDescription = "cancel",
                        onClick = {
                            onCancel()
                            expandedDropdown = false
                        },
                    )
                } else {
                    IconButton(
                        icon = Icons.Rounded.ArrowDropDown,
                        contentDescription = "arrow down",
                        onClick = { expandedDropdown = !expandedDropdown },
                        modifier = Modifier.rotate(if (expandedDropdown) 180f else 0f)
                    )
                }
            },
            onFocus = { expandedDropdown = true }
        )
        if (showEmptyListError || optionList.isNotEmpty()) {
            DropdownMenu(
                expanded = expandedDropdown,
                onDismissRequest = { if (dismissEnabled) expandedDropdown = false },
                properties = PopupProperties(focusable = false, clippingEnabled = false),
                modifier = Modifier.width(with(LocalDensity.current) { dropdownTextInputWidth.toDp() }),
            ) {
                optionList.map {
                    DropdownMenuItem(onClick = {
                        onClickOption(it)
                        if(autoCollapseMenuEnabled) {
                            expandedDropdown = false
                        }
                    }) {
                        Text(it.value)
                    }
                }
            }
        }
    }
}