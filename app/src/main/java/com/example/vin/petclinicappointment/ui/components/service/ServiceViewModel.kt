package com.example.vin.petclinicappointment.ui.components.service

import androidx.lifecycle.viewModelScope
import com.example.vin.petclinicappointment.data.model.Call
import com.example.vin.petclinicappointment.data.model.Service
import com.example.vin.petclinicappointment.data.repository.ServiceRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import com.example.vin.petclinicappointment.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            _userId.value = userRepository.getUserId()
        }
    }

    private val _serviceList = MutableStateFlow<List<Service>>(listOf())
    val serviceList = _serviceList.asStateFlow()

    suspend fun getServiceList(){
        val userId = userId.value
        if(userId !== null) {
            val response = viewModelScope.async(Dispatchers.IO) {
                serviceRepository.getServiceList(
                    clinicId = userId,
                    name = ""
                )
            }.await()
            when (response) {
                is Call.Success -> {
                    var data = response.data?.body()?.data
                    if(data !== null) {
                        data = data.map {
                            it.price = DecimalFormat("0.#").format(it.price).toFloat()
                            it
                        }
                        _serviceList.value = data
                    }else{
                        setMessage(response.data?.body()?.status?.message as String)
                    }
                }
                else ->  setMessage(response.data?.message() as String)
            }
        }
    }
}