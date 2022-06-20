package com.example.vin.petclinicappointment.ui.components.petclinic

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.LocationRepository
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PetClinicInfoViewModel @Inject constructor(
    private val petClinicRepository: PetClinicRepository,
    private val locationRepository: LocationRepository
): BaseViewModel() {
    private val _clinicDetail = MutableStateFlow<PetClinicDetail?>(null)
    val clinicDetail = _clinicDetail.asStateFlow()

    private val _village = MutableStateFlow<Village?>(null)
    val village = _village.asStateFlow()

    private val _district = MutableStateFlow<District?>(null)
    val district = _district.asStateFlow()

    private val _city = MutableStateFlow<City?>(null)
    val city = _city.asStateFlow()

    private val _province = MutableStateFlow<Province?>(null)
    val province = _province.asStateFlow()

    val dayToStringMap = mapOf(
        1 to "senin",
        2 to "selasa",
        3 to "rabu",
        4 to "kamis",
        5 to "jumat",
        6 to "sabtu",
        7 to "minggu"
    )

    suspend fun getPetClinicDetail(id: Int){
        val response = viewModelScope.async(Dispatchers.IO) {
            petClinicRepository.getPetClinicDetail(id)
        }.await()
        when(response){
            is Call.Success -> {
                val data = response.data?.body()?.data
                if(data !== null) {
                    _clinicDetail.value = data
                }else{
                    setMessage(response.data?.body()?.status?.message as String)
                }
            }
            else -> setMessage(response.data?.message() as String)
        }
    }

    suspend fun getVillageDetail(){
        val villageId = clinicDetail.value?.villageId
        if(villageId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getVillageDetail(villageId)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    _village.value = data
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getDistrictDetail(){
        val districtId = village.value?.districtId
        if(districtId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getDistrictDetail(districtId.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    _district.value = data
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getCityDetail(){
        val cityId = district.value?.cityId
        if(cityId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getCityDetail(cityId.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    _city.value = data
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getProvinceDetail(){
        val provinceId = city.value?.provinceId
        if(provinceId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getProvinceDetail(provinceId.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    _province.value = data
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }
}