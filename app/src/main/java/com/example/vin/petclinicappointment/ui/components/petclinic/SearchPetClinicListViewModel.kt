package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.lifecycle.ViewModel
import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchPetClinicListViewModel @Inject constructor(
    private val petClinicRepository: PetClinicRepository
): ViewModel() {

    private val _searchPetClinicListInputValue = MutableStateFlow("")
    val searchPetClinicListInputValue = _searchPetClinicListInputValue.asStateFlow()

    private val _nearbyPetClinicList = MutableStateFlow<List<PetClinic>>(listOf())
    val nearbyPetClinicList = _nearbyPetClinicList.asStateFlow()

    init {
        getNearbyPetClinicList()
    }

    fun setSearchPetClinicListInputValue(value: String){
        _searchPetClinicListInputValue.value = value
    }

    fun getNearbyPetClinicList(){
        _nearbyPetClinicList.value = petClinicRepository.getPetClinicList(searchPetClinicListInputValue.value,"nearby")
    }
}