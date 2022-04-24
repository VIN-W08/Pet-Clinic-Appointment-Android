package com.example.vin.petclinicappointment.data.repository

import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.data.petClinicList

class PetClinicRepository {

    fun getPetClinicList(query: String, type: String): List<PetClinic>{
        return petClinicList
    }
}