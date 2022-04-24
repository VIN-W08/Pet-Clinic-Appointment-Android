package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.lifecycle.ViewModel
import com.example.vin.petclinicappointment.data.model.GeocodingApiResult
import com.example.vin.petclinicappointment.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CurrentLocationMapViewModel @Inject constructor(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _searchLocationValue = MutableStateFlow("")
    val searchLocationValue = _searchLocationValue.asStateFlow()

    private val _selectedLocation = MutableStateFlow<GeocodingApiResult?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _locationRecommendationList = MutableStateFlow<List<GeocodingApiResult>>(listOf())
    val locationRecommendationList = _locationRecommendationList.asStateFlow()

    fun setSearchLocationValue(locationValue: String){
        _searchLocationValue.value = locationValue
    }

    fun setSelectedLocation(location: GeocodingApiResult){
        _selectedLocation.value = location
    }

    suspend fun getAddressAutocompleteList(){
        val response = locationRepository.getAddressAutocompleteList(searchLocationValue.value)
        _locationRecommendationList.value = response ?: listOf()
    }
}