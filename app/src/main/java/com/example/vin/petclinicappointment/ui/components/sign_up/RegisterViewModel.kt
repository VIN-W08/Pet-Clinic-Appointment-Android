package com.example.vin.petclinicappointment.ui.components.sign_up

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.CustomerRepository
import com.example.vin.petclinicappointment.data.repository.LocationRepository
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import com.example.vin.petclinicappointment.ui.components.common.DropdownOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val customerRepository: CustomerRepository,
    private val petClinicRepository: PetClinicRepository,
    private val locationRepository: LocationRepository
): BaseViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _phoneNum = MutableStateFlow("")
    val phoneNum = _phoneNum.asStateFlow()

    private val _address = MutableStateFlow("")
    val address = _address.asStateFlow()

    private val _latitude = MutableStateFlow("")
    val latitude = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow("")
    val longitude = _longitude.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _provinceList = MutableStateFlow(listOf<Province>())
    val provinceList = _provinceList.asStateFlow()

    private val _districtList = MutableStateFlow(listOf<District>())
    val districtList = _districtList.asStateFlow()

    private val _cityList = MutableStateFlow(listOf<City>())
    val cityList = _cityList.asStateFlow()

    private val _villageList = MutableStateFlow(listOf<Village>())
    val villageList = _villageList.asStateFlow()

    private val _selectedVillageId = MutableStateFlow<Long?>(null)
    val selectedVillageId = _selectedVillageId.asStateFlow()

    private val _selectedDistrictId = MutableStateFlow<Long?>(null)
    val selectedDistrictId = _selectedDistrictId.asStateFlow()

    private val _selectedCityId = MutableStateFlow<Long?>(null)
    val selectedCityId = _selectedCityId.asStateFlow()

    private val _selectedProvinceId = MutableStateFlow<Long?>(null)
    val selectedProvinceId = _selectedProvinceId.asStateFlow()

    private val _locationOptionList = MutableStateFlow(listOf<DropdownOption>())
    val locationOptionList = _locationOptionList.asStateFlow()

    fun setLocationOptionList(optionList: List<DropdownOption>){
        _locationOptionList.value = optionList
    }

    fun setSelectedProvinceId(value: Long){
        _selectedProvinceId.value = value
    }

    fun setSelectedDistrictId(value: Long){
        _selectedDistrictId.value = value
    }

    fun setSelectedCityId(value: Long){
        _selectedCityId.value = value
    }

    fun setSelectedVillageId(value: Long){
        _selectedVillageId.value = value
    }

    fun setName(value: String){
        _name.value = value
    }
    fun setEmail(value: String){
        _email.value = value
    }
    fun setPassword(value: String){
        _password.value = value
    }
    fun setPhoneNum(value: String){
        _phoneNum.value = value
    }
    fun setAddress(value: String){
        _address.value = value
    }
    fun setLatitude(value: String){
        _latitude.value = value
    }
    fun setLongitude(value: String){
        _longitude.value = value
    }


    private fun validateUser(role: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
        val passwordPattern = Pattern.compile(passwordRegex)
        if(name.value.trim().isEmpty()){
            setMessage("Nama wajib diinput")
            return false
        }
        if(email.value.trim().isEmpty()){
            setMessage("Email wajib diinput")
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches()){
            setMessage("Format email tidak valid")
            return false
        }
        if(password.value.trim().isEmpty()){
            setMessage("Kata sandi wajib diinput")
            return false
        }
        if(!passwordPattern.matcher(password.value.trim()).matches()){
            setMessage("Kata sandi harus memiliki minimal 8 karakter yang terdiri huruf besar, huruf kecil, dan angka")
            return false
        }
        if(role == "clinic") {
            if(phoneNum.value.trim().isEmpty()){
                setMessage("No. telp wajib diinput")
                return false
            }
            if(address.value.trim().isEmpty()){
                setMessage("Alamat wajib diinput")
                return false
            }
            if(selectedVillageId.value == null){
                setMessage("Kelurahan, kecamatan, kota/kabupatan, dan provinsi wajib diinput")
                return false
            }
            if(latitude.value.trim().isEmpty()){
                setMessage("Lintang wajib diinput")
                return false
            }
            if(longitude.value.trim().isEmpty()){
                setMessage("Bujur wajib diinput")
                return false
            }
        }
        return true
    }

    suspend fun registerCustomer() {
        if(validateUser("customer")) {
            val response = viewModelScope.async(Dispatchers.IO) {
                customerRepository.registerCustomer(RegisterBody(
                    email = email.value,
                    password = password.value,
                    name = name.value
                ))
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data == null) {
                        setMessage(response.data?.body()?.status?.message as String)
                        return
                    }
                    saveUserData(data.customer)
                    userRepository.saveUserRole(data.role)
                    if (data.status) {
                        _isLoggedIn.value = true
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun registerPetClinic(){
        if(validateUser("clinic")) {
            val nameRequest =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name.value)
            val emailRequest =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email.value)
            val passwordRequest =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), password.value)
            val phoneNumRequest =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), phoneNum.value)
            val addressRequest =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), address.value)
            val villageIdRequest = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                selectedVillageId.value.toString())
            val latitudeRequest =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), latitude.value)
            val longitudeRequest =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), longitude.value)
            val response = viewModelScope.async(Dispatchers.IO) {
                petClinicRepository.registerPetClinic(
                    nameRequest,
                    emailRequest,
                    passwordRequest,
                    phoneNumRequest,
                    addressRequest,
                    villageIdRequest,
                    latitudeRequest,
                    longitudeRequest
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data !== null) {
                        saveUserData(data.pet_clinic)
                        userRepository.saveUserRole(data.role)
                        if (data.status) {
                            _isLoggedIn.value = true
                        }
                    }else{
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }

    suspend fun getProvinceList(){
        val response = viewModelScope.async(Dispatchers.IO) {
            locationRepository.getProvinceList()
        }.await()
        when (response) {
            is Call.Success -> {
                val data = response.data?.body()?.provinceList
                if(data !== null) {
                    _provinceList.value = data
                    _locationOptionList.value = data.map {
                        DropdownOption(it.id.toString(), it.name)
                    }
                }
            }
            else -> {
                setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getDistrictList(cityId: Long){
        val response = viewModelScope.async(Dispatchers.IO) {
            locationRepository.getDistrictList(cityId)
        }.await()
        when (response) {
            is Call.Success -> {
                val data = response.data?.body()?.districtList
                if (data !== null) {
                    _districtList.value = data
                    _locationOptionList.value = data.map {
                        DropdownOption(it.id.toString(), it.name)
                    }
                }
            }
            else -> {
                setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getCityList(provinceId: Long){
        val response = viewModelScope.async(Dispatchers.IO) {
            locationRepository.getCityList(provinceId)
        }.await()
        when (response) {
            is Call.Success -> {
                val data = response.data?.body()?.cityList
                if (data !== null) {
                    _cityList.value = data
                    _locationOptionList.value = data.map {
                        DropdownOption(it.id.toString(), it.name)
                    }
                }
            }
            else -> {
                setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun getVillageList(districtId: Long){
        val response = viewModelScope.async(Dispatchers.IO) {
            locationRepository.getVillageList(districtId)
        }.await()
        when (response) {
            is Call.Success -> {
                val data = response.data?.body()?.villageList
                if (data !== null) {
                    _villageList.value = data
                    _locationOptionList.value = data.map {
                        DropdownOption(it.id.toString(), it.name)
                    }
                }
            }
            else -> {
                setMessage(response.data?.message() as String)
            }
        }
    }

    private suspend fun saveUserData(user: User) {
        userRepository.saveUserId(user.id ?: 0)
        userRepository.saveUserName(user.name ?: "")
        userRepository.saveUserEmail(user.email)
        userRepository.saveUserPassword(user.password ?: "")
    }
}