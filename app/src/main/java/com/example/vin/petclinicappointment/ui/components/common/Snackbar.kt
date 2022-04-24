package com.example.vin.petclinicappointment.ui.components.common

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Snackbar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onClickAction: () -> Unit
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->  
            Snackbar(
                content = {
                    Text(
                        snackbarData.message
                    )
                },
                action = {
                    snackbarData.actionLabel?.let {
                        TextButton(onClick = onClickAction) {
                            Text(it)
                        }
                    }
                },
                modifier = modifier.wrapContentHeight(Alignment.Bottom)
            )
        }
    )
}