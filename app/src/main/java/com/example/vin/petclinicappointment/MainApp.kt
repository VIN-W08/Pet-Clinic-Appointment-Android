package com.example.vin.petclinicappointment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.vin.petclinicappointment.ui.components.common.Snackbar
import com.example.vin.petclinicappointment.ui.components.login.LoginPage
import com.example.vin.petclinicappointment.ui.components.main.MainBottomNavBar
import com.example.vin.petclinicappointment.ui.components.main.mainBottomNavGraph
import com.example.vin.petclinicappointment.ui.components.petclinic.CurrentLocationMapPage
import com.example.vin.petclinicappointment.ui.components.petclinic.PetClinicListPage
import com.example.vin.petclinicappointment.ui.components.petclinic.SearchPetClinicListPage
import com.example.vin.petclinicappointment.ui.components.sign_up.SignUpPage

@Composable
fun MainApp() {
    val appState = rememberMainAppState()
    val startDestination = if(appState.getUserAuthStatus()) "main" else "login"
    val scaffoldState = rememberScaffoldState()

    Scaffold (
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        bottomBar = {
            if(appState.showBottomNavBar) {
                MainBottomNavBar(
                    appState.mainBottomNavBarTabs,
                    appState.currentRoute,
                    appState::navigateMainBottomNavBarTo
                )
            }
        }
    ) {
        Box (
            Modifier
                .fillMaxSize()
                .padding(it)
                ){
            NavHost(
                navController = appState.navController,
                startDestination = startDestination
            ) {
                appNavGraph(appState, scaffoldState)
            }
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                snackbarHostState = scaffoldState.snackbarHostState,
                onClickAction = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() }
            )
        }
    }
}

private fun NavGraphBuilder.appNavGraph (appState: MainAppState, scaffoldState: ScaffoldState){
    navigation(
        route = "main",
        startDestination = "home"
    ){
        mainBottomNavGraph(appState, scaffoldState)
    }
    composable(route = "login") {
        LoginPage(
            navigateToHome = { appState.navigateTo("main", "login") },
            navigateToSignUp = { appState.navigateTo("sign-up", "login")}
        )
    }
    composable(route = "sign-up") {
        SignUpPage(
            navigateToLogin = { appState.navigateTo("login", "sign-up")}
        )
    }
    composable(route = "pet-clinic-list/{title}/{type}"){ navBackStackEntry ->
        PetClinicListPage(
            navBackStackEntry.arguments?.getString("title")?:"",
            navBackStackEntry.arguments?.getString("type")?:"",
            navigateBack = appState::navigateBack
        )
    }
    composable(route = "search-pet-clinic-list"){
        SearchPetClinicListPage(
            navigateToCurrentLocationMap = { appState.navigateTo("current-location-map") },
            navigateBack = appState::navigateBack,
            selectedLocationState = appState.selectedLocation
        )
    }
    composable(route = "current-location-map"){
        CurrentLocationMapPage(
            scaffoldState = scaffoldState,
            navigateBack = appState::navigateBack,
            selectedLocationState = appState.selectedLocation,
            deviceLocationState = appState.deviceLocation,
            getDeviceLocation = appState::getDeviceLocation
        )
    }
}