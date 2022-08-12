package com.example.vin.petclinicappointment.ui.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme

@Composable
fun MainBottomNavBar(
    tabs: Array<MainBottomNavTabs>,
    currentRoute: String?,
    navigateBottomNavBarTo: (route: String) -> Unit
){
    BottomNavigation (
        backgroundColor = Color.White
    ){
        tabs.forEach { tab ->
            val title = stringResource(tab.title)
            val selected = tab.route == currentRoute
            val itemColor = if (selected) MaterialTheme.colors.primary else Color.Gray
            BottomNavigationItem(
                selected = selected,
                onClick = { navigateBottomNavBarTo(tab.route) },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            tab.icon,
                            contentDescription = title,
                            tint = itemColor,
                            modifier = Modifier.size(PetClinicAppointmentTheme.dimensions.grid_4)
                        )
                        if(selected) {
                            Text(title, color = itemColor)
                        }
                    }
                }
            )
        }
    }
}