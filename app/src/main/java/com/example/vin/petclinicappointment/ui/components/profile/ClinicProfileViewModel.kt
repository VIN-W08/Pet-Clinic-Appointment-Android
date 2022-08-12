package com.example.vin.petclinicappointment.ui.components.profile

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.UpdateClinicStatusBody
import com.example.vin.petclinicappointment.data.repository.LocationRepository
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository,
    private val petClinicRepository: PetClinicRepository
): BaseViewModel() {
    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            _userId.value = userRepository.getUserId()
        }
    }

    private val _clinicName = MutableStateFlow("")
    val clinicName = _clinicName.asStateFlow()

    private val _clinicEmail = MutableStateFlow("")
    val clinicEmail = _clinicEmail.asStateFlow()

    private val _clinicImage = MutableStateFlow("")
    val clinicImage = _clinicImage.asStateFlow()

    private val _clinicPhoneNum = MutableStateFlow("")
    val clinicPhoneNum = _clinicPhoneNum.asStateFlow()

    private val _clinicAddress = MutableStateFlow("")
    val clinicAddress = _clinicAddress.asStateFlow()

    private val _clinicLatitude = MutableStateFlow<Double?>(null)
    val clinicLatitude = _clinicLatitude.asStateFlow()

    private val _clinicLongitude = MutableStateFlow<Double?>(null)
    val clinicLongitude = _clinicLongitude.asStateFlow()

    private val _clinicVillageId = MutableStateFlow<Long?>(null)
    val clinicVillageId = _clinicVillageId.asStateFlow()

    private val _clinicDistrictId= MutableStateFlow<Long?>(null)
    val clinicDistrictId = _clinicDistrictId.asStateFlow()

    private val _clinicCityId = MutableStateFlow<Long?>(null)
    val clinicCityId = _clinicCityId.asStateFlow()

    private val _clinicProvinceId = MutableStateFlow<Long?>(null)
    val clinicProvinceId = _clinicProvinceId.asStateFlow()

    private val _clinicStatus = MutableStateFlow<Boolean?>(null)
    val clinicStatus = _clinicStatus.asStateFlow()

    private val _clinicLocationName = MutableStateFlow("")
    val clinicLocationName = _clinicLocationName.asStateFlow()

    fun setClinicStatus(value: Boolean){
        _clinicStatus.value = value
    }

    suspend fun getVillageDetail(){
        val clinicVillageId = clinicVillageId.value
        if(clinicVillageId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getVillageDetail(clinicVillageId)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    if (data !== null) {
                        _clinicDistrictId.value = data.districtId.toLong()
                        _clinicLocationName.value = data.name
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getDistrictDetail(){
        val clinicDistrictId = clinicDistrictId.value
        if(clinicDistrictId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getDistrictDetail(clinicDistrictId.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    if (data !== null) {
                        _clinicCityId.value = data.cityId.toLong()
                        _clinicLocationName.value = "${clinicLocationName.value}, ${data.name}"
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getCityDetail(){
        val clinicCityId = clinicCityId.value
        if(clinicCityId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getCityDetail(clinicCityId.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    if (data !== null) {
                        _clinicProvinceId.value = data.provinceId.toLong()
                        _clinicLocationName.value = "${clinicLocationName.value}, ${data.name}"
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getProvinceDetail() {
        val clinicProvinceId = clinicProvinceId.value
        if (clinicProvinceId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getProvinceDetail(clinicProvinceId)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    if (data !== null) {
                        _clinicLocationName.value = "${clinicLocationName.value}, ${data.name}"
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getPetClinicDetail() {
        val userId = userId.value
        if (userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                petClinicRepository.getPetClinicDetail(userId)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data !== null) {
                        _clinicName.value = data.name ?: ""
                        _clinicEmail.value = data.email ?: ""
                        _clinicImage.value = data.image ?: ""
                        _clinicPhoneNum.value = data.phoneNum ?: ""
                        _clinicAddress.value = data.address ?: ""
                        _clinicVillageId.value = data.villageId
                        _clinicLatitude.value = data.latitude
                        _clinicLongitude.value = data.longitude
                        _clinicStatus.value = data.status
                    } else {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun updateClinicStatus(status: Boolean): Boolean {
        val userId = userId.value
        if (userId !== null) {
            val body = UpdateClinicStatusBody(status = status)
            val response = viewModelScope.async(Dispatchers.IO) {
                petClinicRepository.updateStatus(userId, body)
            }.await()
            return when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data !== null) {
                        _clinicStatus.value = data.status
                    } else {
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                    data !== null
                }
                else -> {
                    setMessage(response.data?.message() as String)
                    return false
                }
            }
        }
        return false
    }

    suspend fun logout() = userRepository.logout()
}