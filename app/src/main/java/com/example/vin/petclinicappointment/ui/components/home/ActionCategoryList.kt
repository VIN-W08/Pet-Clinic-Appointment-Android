package com.example.vin.petclinicappointment.ui.components.home

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.launch
import com.example.vin.petclinicappointment.ui.utils.rememberWindowInfo
import com.example.vin.petclinicappointment.ui.components.common.View

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionCategoryList(
    modalBottomSheetState: ModalBottomSheetState,
    selectedActionCategoryListState: MutableState<String>,
    title: String,
    categoryList: List<ActionCategory>,
    navigateToPage: (route: String) -> Unit,
    baseRoute: String
) {
    val windowInfo = rememberWindowInfo()
    val coroutineScope = rememberCoroutineScope()
    val windowWidth = windowInfo.width.size.toString().substringBefore(".dp").toFloat().toInt()
    val rowItemCount = (windowWidth + (windowWidth / 2)) / 100

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(PetClinicAppointmentTheme.dimensions.grid_2),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.h1
            )
            View(
               Modifier
                   .background(PetClinicAppointmentTheme.colors.primaryVariant, RoundedCornerShape(20))
                   .padding(
                       horizontal = PetClinicAppointmentTheme.dimensions.grid_1_5,
                       vertical = PetClinicAppointmentTheme.dimensions.grid_0_25
                   ),
                onClick = {
                    coroutineScope.launch {
                        selectedActionCategoryListState.value = title
                        modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                    }
                }
            ) {
                Text(
                    "More",
                        color = PetClinicAppointmentTheme.colors.onPrimary,
                        style = PetClinicAppointmentTheme.typography.body2,
                        fontWeight = FontWeight.SemiBold,
                )
            }
        }
        LazyRow(
            Modifier.padding(
                start = PetClinicAppointmentTheme.dimensions.grid_2,
                bottom = PetClinicAppointmentTheme.dimensions.grid_2
            ),
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            items(
                if (categoryList.size < rowItemCount) categoryList
                else categoryList.subList(0, rowItemCount - 1)
            ) { actionCategory ->
                ActionCategoryItem(
                    actionCategory
                ) {
                    navigateToPage("$baseRoute/${actionCategory.title}/${actionCategory.type}")
                }
            }
        }
    }
}

@Composable
fun ActionCategoryItem(
    actionCategory: ActionCategory,
    navigateToPage: () -> Unit
) {
    val interactionSource = MutableInteractionSource()

    Column(
        Modifier.width(PetClinicAppointmentTheme.dimensions.grid_6 * 2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            Modifier
                .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_1)
                .size(PetClinicAppointmentTheme.dimensions.grid_6 * 2)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = navigateToPage
                ),
            border = BorderStroke(PetClinicAppointmentTheme.dimensions.grid_0_125, PetClinicAppointmentTheme.colors.primaryVariant),
            shape = RoundedCornerShape(10.dp),
//            elevation = 8.dp
        ) {
            Image(
                painter = painterResource(id = actionCategory.image),
                contentDescription = actionCategory.description,
                contentScale = ContentScale.Fit
            )
        }
        Text(
            actionCategory.title,
            style = PetClinicAppointmentTheme.typography.h3,
            textAlign = TextAlign.Center
        )
    }
}