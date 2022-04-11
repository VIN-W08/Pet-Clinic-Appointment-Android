package com.example.vin.petclinicappointment

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.vin.petclinicappointment.ui.components.login.LoginPage
import com.example.vin.petclinicappointment.ui.components.sign_up.SignUpPage

@Composable
fun MainApp() {
    val appState = rememberMainAppState()
    val startDestination = if(appState.getUserAuthStatus()) "main" else "login"

    Scaffold {
        NavHost(
            navController = appState.navController,
            startDestination = startDestination
        ) {
            appNavGraph(appState)
        }
    }
}

private fun NavGraphBuilder.appNavGraph (appState: MainAppState){
    navigation(
        route = "main",
        startDestination = "home"
    ){

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
}