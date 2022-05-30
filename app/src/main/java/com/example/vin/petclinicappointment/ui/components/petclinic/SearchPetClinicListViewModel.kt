package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.Coordinate
import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPetClinicListViewModel @Inject constructor(
    private val petClinicRepository: PetClinicRepository
): BaseViewModel() {

    private val _searchPetClinicListInputValue = MutableStateFlow("")
    val searchPetClinicListInputValue = _searchPetClinicListInputValue.asStateFlow()

    private val _nearbyPetClinicList = MutableStateFlow<List<PetClinic>>(listOf())
    val nearbyPetClinicList = _nearbyPetClinicList.asStateFlow()

    @OptIn(FlowPreview::class)
    fun setSearchPetClinicListInputValue(value: String){
        _searchPetClinicListInputValue.value = value
    }

    suspend fun getNearbyPetClinicList(coordinate: Coordinate? = null){
        val response = viewModelScope.async(Dispatchers.IO) {
            petClinicRepository.getPetClinicList(
                searchPetClinicListInputValue.value,
                coordinate?.latitude,
                coordinate?.longitude
            )
        }.await()
        when(response) {
            is Call.Success -> {
                val data = response.data?.body()?.data
                if (!data.isNullOrEmpty()) {
                    _nearbyPetClinicList.value = data
                } else {
                    _nearbyPetClinicList.value = listOf()
                }
            }
            else -> setMessage(response.data?.message() as String)
        }
    }
}