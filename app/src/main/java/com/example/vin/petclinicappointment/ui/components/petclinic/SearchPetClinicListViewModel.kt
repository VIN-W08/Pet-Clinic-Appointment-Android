package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.vin.petclinicappointment.data.datasource.PetClinicDataSource
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchPetClinicListViewModel @Inject constructor(
    private val petClinicRepository: PetClinicRepository
): BaseViewModel() {

    private val _searchPetClinicListInputValue = MutableStateFlow("")
    val searchPetClinicListInputValue = _searchPetClinicListInputValue.asStateFlow()

    private val _selectedCoordinate = MutableStateFlow<LatLng?>(null)
    val selectedCoordinate = _selectedCoordinate.asStateFlow()

    fun setSelectedCoordinate(coordinate: LatLng?){
        _selectedCoordinate.value = coordinate
    }

    fun setSearchPetClinicListInputValue(value: String){
        _searchPetClinicListInputValue.value = value
    }

    val sortedClinicList = listOf(_searchPetClinicListInputValue, selectedCoordinate).merge().flatMapLatest {
        Pager(PagingConfig(pageSize = 10, prefetchDistance = 1)) {
                PetClinicDataSource(
                    petClinicRepository,
                    searchPetClinicListInputValue.value,
                    selectedCoordinate.value?.latitude,
                    selectedCoordinate.value?.longitude
                )
        }.flow.debounce(1000)
    }
}