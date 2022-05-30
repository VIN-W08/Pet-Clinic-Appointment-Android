package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PetClinicListPage(
    title: String,
    listType: String,
    navigateBack: () -> Unit
){
    Surface(
        color = MaterialTheme.colors.background
    ){
        Column {
            Row(
                Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigateBack() }) {
                    Icon(imageVector = Icons.Default.NavigateBefore,
                        contentDescription = "arrow_back")
                }
                Row {
                    Text(
                        title,
                        style = MaterialTheme.typography.h2
                    )
                }
            }
//                PetClinicList(petClinicList)
        }
    }
}