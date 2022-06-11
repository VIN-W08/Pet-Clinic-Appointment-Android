package com.example.vin.petclinicappointment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vin.petclinicappointment.data.UserDataStore
import com.example.vin.petclinicappointment.data.model.GeocodingApiResult
import com.example.vin.petclinicappointment.data.repository.LocationRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.components.main.MainBottomNavTabs
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    userDataStore : UserDataStore = UserDataStore (LocalContext.current),
    userRepository: UserRepository = UserRepository(userDataStore),
    locationRepository: LocationRepository = LocationRepository(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MainAppState {
   return remember(navController) {
        MainAppState(navController, scaffoldState, userRepository, locationRepository, coroutineScope)
    }
}

class MainAppState(
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    val userRepository: UserRepository,
    val locationRepository: LocationRepository,
    val coroutineScope: CoroutineScope
){
    var userRole: MutableState<String?> = mutableStateOf(null)

    val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val selectedLocation: MutableState<GeocodingApiResult?> = mutableStateOf(null)
    val deviceCoordinate: MutableState<LatLng?> = mutableStateOf(null)
    val deviceLocation: MutableState<GeocodingApiResult?> = mutableStateOf(null)

    private val _mainBottomNavBarTabs = MutableStateFlow<Array<MainBottomNavTabs>>(arrayOf())
    val mainBottomNavBarTabs = _mainBottomNavBarTabs.asStateFlow()

    private val _mainBottomNavBarRoutes = MutableStateFlow(listOf<String>())
    val mainBottomNavBarRoutes = _mainBottomNavBarRoutes.asStateFlow()

    val showBottomNavBar: Boolean
       @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route in mainBottomNavBarRoutes.value

    val currentRoute: String?
            get() = navController.currentDestination?.route

    fun navigateMainBottomNavBarTo(route: String){
        if(route != currentRoute) {
            navController.navigate(route) {
                if (!popUpToRoute.isNullOrEmpty()) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    fun setMainBottomNav(){
        coroutineScope.launch {
            val role = userRepository.getUserRole()
            _mainBottomNavBarTabs.value = MainBottomNavTabs.create(role ?: "")
            _mainBottomNavBarRoutes.value = mainBottomNavBarTabs.value.map { it.route }
        }
    }

    fun getUserRole(): String {
        val role = runBlocking {
            val userRole = async { userRepository.getUserRole() }
            userRole.await()
        }
        return role ?: ""
    }

    private fun getUpdatingDeviceCoordinate(context: Context, onLocationResult: (locationResult: LocationResult) -> Unit){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                onLocationResult(locationResult)
            }
        }
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper())
    }

    fun getDeviceCoordinate(
        context: Context,
        onSuccessListener: (locationResult: Location) -> Unit,
        onFailListener: () -> Unit
    ) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val tokenSource = CancellationTokenSource()
        val task = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
                tokenSource.token)
        task.addOnSuccessListener {
            onSuccessListener(it)
        }
        task.addOnFailureListener {
            onFailListener()
        }
    }

    fun getLocation(context: Context, onFail: () -> Unit){
        getDeviceCoordinate(
            context,
            onSuccessListener = {
                deviceCoordinate.value = LatLng(
                    it.latitude,
                    it.longitude
                )
                getReverseGeocodingLocation() },
            onFailListener = onFail
        )
    }

    fun getDeviceLocation(context: Context, onFail: () -> Unit){
        getDeviceCoordinate(
            context,
            onSuccessListener = {
                deviceCoordinate.value = LatLng(
                    it.latitude,
                    it.longitude
                )
                getReverseGeocodingDeviceLocation()},
        onFailListener = onFail)
    }

    fun getReverseGeocodingDeviceLocation(){
        coroutineScope.launch(Dispatchers.IO) {
            val currentLocation =
                locationRepository.getReverseGeocodingLocation(deviceCoordinate.value as LatLng)
            if (currentLocation !== null) {
                deviceLocation.value = currentLocation
            }
        }
    }

    fun getReverseGeocodingLocation(){
        coroutineScope.launch(Dispatchers.IO) {
            val currentLocation =
                locationRepository.getReverseGeocodingLocation(deviceCoordinate.value as LatLng)
            if (currentLocation !== null) {
                deviceLocation.value = currentLocation
                selectedLocation.value = currentLocation
            }
        }
    }

    fun getGpsEnabledStatus(
        context: Context,
        onEnabled: () -> Unit,
        onDisabled: (e: Exception) -> Unit
    ){
        val locationSettingBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(locationSettingBuilder.build())
        task.addOnSuccessListener { locationSettingResponse ->
            val locationSettingStates = locationSettingResponse.locationSettingsStates
            if(locationSettingStates !== null) {
                onEnabled()
            }
        }
        task.addOnFailureListener { exception ->
            onDisabled(exception)
        }
    }

    fun getLocationPermissionStatus(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED

    fun getUserAuthStatus(): Boolean {
        val auth = runBlocking {
            val userEmail = async { userRepository.getUserEmail() }
            val userPassword = async { userRepository.getUserPassword() }
            userEmail.await()!==null && userPassword.await()!==null
        }
        return auth
    }

    fun navigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusiveCurrent: Boolean = true,
    ){
        navController.navigate(route) {
            if(!popUpToRoute.isNullOrEmpty()) {
                popUpTo(popUpToRoute) { inclusive = inclusiveCurrent }
                launchSingleTop = true
            }
        }
    }

    fun navigateBack(){
        navController.popBackStack()
    }
}

