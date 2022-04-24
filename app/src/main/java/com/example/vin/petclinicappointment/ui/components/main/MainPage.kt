package com.example.vin.petclinicappointment.ui.components.main

import androidx.annotation.StringRes
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.vin.petclinicappointment.MainAppState
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.ui.components.home.HomePage

fun NavGraphBuilder.mainBottomNavGraph (appState: MainAppState, scaffoldState: ScaffoldState){
    composable(route = "home") {
        HomePage(
            scaffoldState = scaffoldState,
            navigateToSearchPetClinic = { appState.navigateTo("search-pet-clinic-list")},
            navigateTo = appState::navigateTo,
            getLocation = appState::getLocation,
            getLocationPermissionStatus = appState::getLocationPermissionStatus,
            getGpsEnabledStatus = appState::getGpsEnabledStatus,
        )
    }
    composable(route = "history") {}
    composable(route = "profile"){}
}

enum class MainBottomNavTabs(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
) {
    Home(R.string.tab_home, Icons.Default.Home, "home"),
    History(R.string.tab_history, Icons.Default.History,"history"),
    Profile(R.string.tab_profile, Icons.Default.AccountCircle, "profile")
}

@Composable
fun MainPage() {
}