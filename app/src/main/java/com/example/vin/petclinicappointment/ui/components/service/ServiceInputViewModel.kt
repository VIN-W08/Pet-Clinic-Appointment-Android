package com.example.vin.petclinicappointment.ui.components.service

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.*
import com.example.vin.petclinicappointment.data.repository.ServiceRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import retrofit2.Response
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ServiceInputViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _serviceId = MutableStateFlow<Int?>(null)
    val serviceId = _serviceId.asStateFlow()

    private val _serviceDetail = MutableStateFlow<ServiceDetail?>(null)
    val serviceDetail = _serviceDetail.asStateFlow()

    private val _serviceName = MutableStateFlow("")
    val serviceName = _serviceName.asStateFlow()

    private val _servicePrice = MutableStateFlow("")
    val servicePrice = _servicePrice.asStateFlow()

    private val _serviceStatus = MutableStateFlow(false)
    val serviceStatus = _serviceStatus.asStateFlow()

    fun setServiceId(value: Int){
        _serviceId.value = value
    }

    fun setServiceName(value: String){
        _serviceName.value = value
    }

    fun setServicePrice(value: String){
        _servicePrice.value = value
    }

    fun setServiceStatus(value: Boolean){
        _serviceStatus.value = value
    }

    fun populateServiceData(){
        val serviceDetail = serviceDetail.value
        if(serviceDetail !== null) {
            val decimalFormat = DecimalFormat("0.#")
            _serviceName.value = serviceDetail.name
            _servicePrice.value = decimalFormat.format(serviceDetail.price)
            _serviceStatus.value = serviceDetail.status
        }
    }

    private fun validateServiceInput(): Boolean{
        if(serviceName.value.isEmpty()){
            setMessage("Nama layanan wajib diinput")
            return false
        }else if(servicePrice.value.isEmpty()){
            setMessage("Harga layanan wajib diinput")
            return false
        }
        return true
    }

    suspend fun getServiceDetail(){
        val serviceId = serviceId.value
        if(serviceId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceRepository.getServiceDetail(serviceId)
            }.await()
            when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    if (data !== null) {
                        _serviceDetail.value = data
                    }
                }
                else -> {
                    val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                    setMessage(errorJson.getJSONObject("status").getString("message"))
                }
            }
        }
    }

    suspend fun updateService(): Boolean {
        if(validateServiceInput()) {
            val userId = userRepository.getUserId()
            val serviceId = serviceId.value
            if (userId !== null && serviceId !== null) {
                val response = viewModelScope.async(Dispatchers.IO) {
                    serviceRepository.updateService(
                        serviceId = serviceId,
                        body = UpdateServiceBody(
                            name = serviceName.value,
                            price = servicePrice.value.toFloat(),
                            status = serviceStatus.value
                        )
                    )
                }.await()
                return when (response) {
                    is Call.Success -> {
                        val data = response.data?.body()?.data
                        data !== null
                    }
                    else -> {
                        val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                        setMessage(errorJson.getJSONObject("status").getString("message"))
                        false
                    }
                }
            }
            return false
        }
        return false
    }

    suspend fun addService(): Boolean{
        if(validateServiceInput()) {
            val userId = userRepository.getUserId()
            if (userId !== null) {
                val response = viewModelScope.async(Dispatchers.IO) {
                    serviceRepository.addService(
                        CreateServiceBody(
                            petClinicId = userId,
                            name = serviceName.value,
                            price = servicePrice.value.toFloat()
                        )
                    )
                }.await()
                return when (response) {
                    is Call.Success -> {
                        val data = response.data?.body()?.data
                        data !== null
                    }
                    else -> {
                        val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                        setMessage(errorJson.getJSONObject("status").getString("message"))
                        false
                    }
                }
            }
            return false
        }
        return false
    }

    suspend fun deleteService(): Boolean {
        val serviceId = serviceId.value
        if (serviceId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceRepository.deleteService(serviceId)
            }.await()
            return when (response) {
                is Call.Success -> {
                    val data = response.data?.body()?.data
                    data !== null
                }
                else -> {
                    val errorJson = JSONObject((response.data as Response<ResponseStatus>).errorBody()?.string())
                    setMessage(errorJson.getJSONObject("status").getString("message"))
                    false
                }
            }
        }
        return false
    }
}