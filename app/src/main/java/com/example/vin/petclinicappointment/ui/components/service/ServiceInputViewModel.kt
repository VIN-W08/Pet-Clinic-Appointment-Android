package com.example.vin.petclinicappointment.ui.components.service

import android.util.Log
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
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ServiceInputViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _serviceId = MutableStateFlow<Int?>(null)
    val serviceId = _serviceId.asStateFlow()

    private val _service = MutableStateFlow<Service?>(null)
    val service = _service.asStateFlow()

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
        val service = service.value
        if(service !== null) {
            val decimalFormat = DecimalFormat("0.#")
            _serviceName.value = service.name
            _servicePrice.value = decimalFormat.format(service.price)
            _serviceStatus.value = service.status
        }
    }

    private fun validateServiceInput(): Boolean{
        if(serviceName.value.trim().isEmpty()){
            setMessage("Nama layanan wajib diinput")
            return false
        }else if(servicePrice.value.trim().isEmpty()){
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
                        _service.value = data
                    }else{
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else -> setMessage(response.data?.message() as String)
            }
        }
    }

    suspend fun updateService(): Boolean {
        if(validateServiceInput()) {
            val serviceId = serviceId.value
            if (serviceId !== null) {
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
                        if(data == null){
                            setMessage(response.data?.body()?.status?.message as String)
                        }
                        data !== null
                    }
                    else -> {
                        setMessage(response.data?.message() as String)
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
                        if(data == null){
                            setMessage(response.data?.body()?.status?.message as String)
                        }
                        data !== null
                    }
                    else -> {
                        setMessage(response.data?.message() as String)
                        false
                    }
                }
            }
            return false
        }
        Log.d("debug1", "in validate false")
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
                    if(data == null){
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                    data !== null
                }
                else -> {
                    setMessage(response.data?.message() as String)
                    false
                }
            }
        }
        return false
    }
}