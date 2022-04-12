package com.example.vin.petclinicappointment.ui.components.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.data.actionCategoryList
import com.example.vin.petclinicappointment.ui.components.common.ModalBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage(
    navigateToSearchPetClinic: () -> Unit,
    navigateTo: (route: String) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { state -> state != ModalBottomSheetValue.HalfExpanded}
    )
    val selectedActionCategoryList = rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val localFocusManager = LocalFocusManager.current

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    coroutineScope.launch {
                        if (modalBottomSheetState.isVisible) {
                            modalBottomSheetState.hide()
                        }
                    }
                    localFocusManager.clearFocus()
                })
            },
        color = MaterialTheme.colors.background
            ){
        Column {
            HomeHeader(navigateToPetClinicList = navigateToSearchPetClinic)
            ModalBottomSheet(
                modifier = Modifier.padding(bottom = 55.dp),
                modalBottomSheetState = modalBottomSheetState,
                sheetContent = { MoreCategoryContent(selectedActionCategoryList.value) }
            ){
                Column {
                    ActionCategoryList(
                        modalBottomSheetState,
                        selectedActionCategoryList,
                        "Klinik",
                        actionCategoryList,
                        navigateTo,
                        "pet-clinic-list"
                    )
                    ActionCategoryList(
                        modalBottomSheetState,
                        selectedActionCategoryList,
                        "Layanan",
                        actionCategoryList,
                        navigateTo,
                        "pet-clinic-list"
                    )
                }
            }
        }
    }
}