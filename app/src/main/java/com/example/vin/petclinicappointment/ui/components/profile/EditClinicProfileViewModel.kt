package com.example.vin.petclinicappointment.ui.components.profile

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.ResponseStatus
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditClinicProfileViewModel @Inject constructor(
    private val petClinicRepository: PetClinicRepository,
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository
): BaseViewModel() {
    private val _clinicName = MutableStateFlow("")
    val clinicName = _clinicName.asStateFlow()

    private val _clinicEmail = MutableStateFlow("")
    val clinicEmail = _clinicEmail.asStateFlow()

    private val _initialClinicImage = MutableStateFlow("")
    val initialClinicImage = _initialClinicImage.asStateFlow()

    private val _clinicPhoneNum = MutableStateFlow("")
    val clinicPhoneNum = _clinicPhoneNum.asStateFlow()

    private val _clinicAddress = MutableStateFlow("")
    val clinicAddress = _clinicAddress.asStateFlow()

    private val _clinicLatitude = MutableStateFlow("")
    val clinicLatitude = _clinicLatitude.asStateFlow()

    private val _clinicLongitude = MutableStateFlow("")
    val clinicLongitude = _clinicLongitude.asStateFlow()

    private val _selectedVillageOption = MutableStateFlow<DropdownOption?>(null)
    val selectedVillageOption = _selectedVillageOption.asStateFlow()

    private val _selectedDistrictOption = MutableStateFlow<DropdownOption?>(null)
    val selectedDistrictOption = _selectedDistrictOption.asStateFlow()

    private val _selectedCityOption = MutableStateFlow<DropdownOption?>(null)
    val selectedCityOption = _selectedCityOption.asStateFlow()

    private val _selectedProvinceOption = MutableStateFlow<DropdownOption?>(null)
    val selectedProvinceOption = _selectedProvinceOption.asStateFlow()

    private val _locationOptionList = MutableStateFlow(listOf<DropdownOption>())
    val locationOptionList = _locationOptionList.asStateFlow()

    private val _clinicImage = MutableStateFlow<Uri?>(null)
    val clinicImage = _clinicImage.asStateFlow()

    fun setClinicName(value: String){
        _clinicName.value = value
    }

    fun setClinicEmail(value: String){
        _clinicEmail.value = value
    }

    fun setClinicPhoneNum(value: String){
        _clinicPhoneNum.value = value
    }

    fun setClinicLatitude(value: String){
        _clinicLatitude.value = value
    }

    fun setClinicLongitude(value: String){
        _clinicLongitude.value = value
    }

    fun setSelectedVillageOption(value: DropdownOption?){
        _selectedVillageOption.value = value
    }

    fun setSelectedDistrictOption(value: DropdownOption?){
        _selectedDistrictOption.value = value
    }

    fun setSelectedCityOption(value: DropdownOption?){
        _selectedCityOption.value = value
    }

    fun setSelectedProvinceOption(value: DropdownOption?){
        _selectedProvinceOption.value = value
    }

    fun setLocationOptionList(optionList: List<DropdownOption>){
        _locationOptionList.value = optionList
    }

    fun setClinicAddress(value: String){
        _clinicAddress.value = value
    }

    fun setClinicImage(value: Uri?){
        _clinicImage.value = value
    }

    fun setInitialClinicImage(value: String){
        _initialClinicImage.value = value
    }

    private fun validateClinic(): Boolean{
        if(clinicName.value.trim().isEmpty()){
            setMessage("Nama wajib diinput")
            return false
        }
        if(clinicEmail.value.trim().isEmpty()){
            setMessage("Email wajib diinput")
            return false
        }
        if(clinicPhoneNum.value.trim().isEmpty()){
            setMessage("No. telp wajib diinput")
            return false
        }
        if(clinicAddress.value.trim().isEmpty()){
            setMessage("Alamat wajib diinput")
            return false
        }
        if(selectedVillageOption.value == null ||
            selectedDistrictOption.value == null ||
            selectedCityOption.value == null ||
                selectedProvinceOption.value == null){
            setMessage("Kecamatan, Kelurahan, Kota/Kabupaten, dan Provinsi wajib diinput")
            return false
        }
        if(clinicLatitude.value.trim().isEmpty()){
            setMessage("Lintang wajib diinput")
            return false
        }
        if(clinicLongitude.value.trim().isEmpty()){
            setMessage("Bujur wajib diinput")
            return false
        }
        return true
    }

    suspend fun getClinicData(){
        userRepository.let {
            _clinicName.value = it.getUserName() ?: ""
            _clinicEmail.value = it.getUserEmail() ?: ""
            _initialClinicImage.value = it.getUserImage() ?: ""
            _clinicPhoneNum.value = it.getUserPhoneNum() ?: ""
            _clinicAddress.value = it.getUserAddress() ?: ""
            val selectedVillageId =  it.getUserVillageId()
            _selectedVillageOption.value = DropdownOption(
                if(selectedVillageId !== null) selectedVillageId.toString() else "",
                ""
            )
            val clinicLatitude = it.getUserLatitude()
            val clinicLongitude = it.getUserLongitude()
            _clinicLatitude.value = if (clinicLatitude !== null) clinicLatitude.toString() else ""
            _clinicLongitude.value =
                if (clinicLongitude !== null) clinicLongitude.toString() else ""
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

    suspend fun getVillageDetail(){
        val selectedVillageOption = selectedVillageOption.value
        if(selectedVillageOption !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getVillageDetail(selectedVillageOption.id.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    if (data !== null) {
                        _selectedVillageOption.value = DropdownOption(data.id.toString(), data.name)
                        _selectedDistrictOption.value = DropdownOption(data.districtId, "")
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }

    suspend fun getDistrictDetail(){
        val selectedDistrictOption = selectedDistrictOption.value
        if(selectedDistrictOption !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getDistrictDetail(selectedDistrictOption.id.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    if (data !== null) {
                        _selectedDistrictOption.value = DropdownOption(data.id.toString(), data.name)
                        _selectedCityOption.value = DropdownOption(data.cityId, "")
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }

    suspend fun getCityDetail(){
        val selectedCityOption = selectedCityOption.value
        if(selectedCityOption !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getCityDetail(selectedCityOption.id.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    if (data !== null) {
                        _selectedCityOption.value = DropdownOption(data.id.toString(), data.name)
                        _selectedProvinceOption.value = DropdownOption(data.provinceId, "")
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }

    suspend fun getProvinceDetail() {
        val selectedProvinceOption = selectedProvinceOption.value
        if (selectedProvinceOption !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                locationRepository.getProvinceDetail(selectedProvinceOption.id.toLong())
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()
                    if (data !== null) {
                        _selectedProvinceOption.value =
                            DropdownOption(data.id.toString(), data.name)
                    }
                }
                else -> {
                    setMessage(response.data?.message() as String)
                }
            }
        }
    }

    suspend fun updateClinic(context: Context): Boolean {
        if(validateClinic()) {
            val userId = userRepository.getUserId()
            val selectedVillageOption = selectedVillageOption.value
            if (userId !== null && selectedVillageOption !== null) {
                val nameRequest =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), clinicName.value)
                val emailRequest =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), clinicEmail.value)
                val phoneNumRequest = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                    clinicPhoneNum.value)
                val addressRequest = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                    clinicAddress.value)
                val villageIdRequest = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                    selectedVillageOption.id)
                val latitudeRequest = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                    clinicLatitude.value)
                val longitudeRequest = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                    clinicLongitude.value)
                if (clinicImage.value !== null) {
                    val imageFile = clinicImage.value?.let { getFile(context, it) }
                    if (imageFile !== null) {
                        val imageRequest =
                            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
                        val filePart = MultipartBody.Part.createFormData(
                            "image",
                            imageFile.name,
                            imageRequest
                        )
                        val response = viewModelScope.async(Dispatchers.IO) {
                            petClinicRepository.updatePetClinic(
                                userId,
                                nameRequest,
                                emailRequest,
                                phoneNumRequest,
                                addressRequest,
                                villageIdRequest,
                                latitudeRequest,
                                longitudeRequest,
                                filePart
                            )
                        }.await()
                        return when (response) {
                            is Call.Success -> {
                                val data = response.data?.body()?.data
                                if (data !== null) {
                                    _clinicName.value = data.name ?: ""
                                    _clinicEmail.value = data.email ?: ""
                                    _clinicAddress.value = data.address ?: ""
                                    _clinicPhoneNum.value = data.phoneNum ?: ""
                                    _clinicLatitude.value =
                                        if (data.latitude !== null) data.latitude.toString() else ""
                                    _clinicLongitude.value =
                                        if (data.longitude !== null) data.longitude.toString() else ""
                                    _selectedVillageOption.value =
                                        if (data.villageId !== null) DropdownOption(data.villageId.toString(),
                                            "") else null
                                    _initialClinicImage.value = data.image ?: ""
                                    _clinicImage.value = null
                                } else {
                                    setMessage(response.data?.body()?.status?.message as String)
                                }
                                data !== null
                            }
                            else -> {
                                val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                                setMessage(errorJson.getJSONObject("status").getString("message"))
                                false
                            }
                        }
                    }
                } else {
                    if(initialClinicImage.value.isEmpty() && clinicImage.value == null) {
                        val storedClinicImage = userRepository.getUserImage()
                        if(storedClinicImage !== null) {
                            if (storedClinicImage.isNotEmpty()) {
                                val deleteImageResponse = viewModelScope.async(Dispatchers.IO) {
                                    petClinicRepository.deletePetClinicImage(userId)
                                }.await()
                                when (deleteImageResponse) {
                                    is Call.Success -> {
                                        val data = deleteImageResponse.data?.body()?.data
                                        if (data !== null) {
                                            _initialClinicImage.value = data.image ?: ""
                                            _clinicImage.value = null
                                        } else {
                                            setMessage(deleteImageResponse.data?.body()?.status?.message as String)
                                        }
                                    }
                                    else -> {
                                        val errorJson =
                                            JSONObject((deleteImageResponse.data as Response<ResponseStatus>).errorBody()
                                                ?.string())
                                        setMessage(errorJson.getJSONObject("status")
                                            .getString("message"))
                                        return false
                                    }
                                }
                            }
                        }
                    }
                    val response = viewModelScope.async(Dispatchers.IO) {
                        petClinicRepository.updatePetClinic(
                            userId,
                            nameRequest,
                            emailRequest,
                            phoneNumRequest,
                            addressRequest,
                            villageIdRequest,
                            latitudeRequest,
                            longitudeRequest,
                            null
                        )
                    }.await()
                    return when (response) {
                        is Call.Success -> {
                            val data = response.data?.body()?.data
                            if (data !== null) {
                                _clinicName.value = data.name ?: ""
                                _clinicEmail.value = data.email ?: ""
                                _clinicAddress.value = data.address ?: ""
                                _clinicPhoneNum.value = data.phoneNum ?: ""
                                _clinicLatitude.value =
                                    if (data.latitude !== null) data.latitude.toString() else ""
                                _clinicLongitude.value =
                                    if (data.longitude !== null) data.longitude.toString() else ""
                                _selectedVillageOption.value =
                                    if (data.villageId !== null) DropdownOption(data.villageId.toString(),
                                        "") else null
                                _initialClinicImage.value = data.image ?: ""
                                _clinicImage.value = null
                            } else {
                                setMessage(response.data?.body()?.status?.message as String)
                            }
                            data !== null
                        }
                        else -> {
                            val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                            setMessage(errorJson.getJSONObject("status").getString("message"))
                            false
                        }
                    }
                }
            }
            return false
        }
        return false
    }

    suspend fun saveClinic(){
        userRepository.saveUserName(clinicName.value)
        userRepository.saveUserEmail(clinicEmail.value)
        userRepository.saveUserImage(initialClinicImage.value)
        userRepository.saveUserPhoneNum(clinicPhoneNum.value)
        userRepository.saveUserAddress(clinicAddress.value)
        val selectedVillageOption = selectedVillageOption.value
        if(selectedVillageOption !== null) {
            if(selectedVillageOption.id.trim().isNotEmpty()) userRepository.saveUserVillageId(selectedVillageOption.id.toLong())
            else userRepository.saveUserVillageId(0)
        }
        userRepository.saveUserLatitude(if(clinicLatitude.value.trim().isNotEmpty()) clinicLatitude.value.toDouble() else 0.0)
        userRepository.saveUserLongitude(if(clinicLongitude.value.trim().isNotEmpty()) clinicLongitude.value.toDouble() else 0.0)
    }

    private fun getFile(context: Context, uri: Uri): File? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            context.contentResolver.query(uri, projection, null, null, null)
                ?: return null
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()

        val s = cursor.getString(columnIndex)
        cursor.close()
        return File(s)
    }
}