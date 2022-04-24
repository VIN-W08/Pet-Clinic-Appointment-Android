package com.example.vin.petclinicappointment.data

import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.data.model.User
import com.example.vin.petclinicappointment.ui.components.home.ActionCategory
import com.example.vin.petclinicappointment.ui.theme.Topaz
import com.example.vin.petclinicappointment.ui.theme.TrueBlue

val dummyUser = User("test1@gmail.com", "password1", "0", "Test 1", "Street Test 1")

val actionCategoryList = listOf(
    ActionCategory(
        "nearest_clinic",
        "Terdekat 1" ,
        R.drawable.register_success,
        "nearest_clinic",
        Topaz
    ),
    ActionCategory(
        "popular_clinic",
        "Terpopuler 2",
        R.drawable.nearest_clinic_image,
        "popular_clinic",
        TrueBlue
    ),
    ActionCategory(
        "nearest_clinic",
        "Terdekat 3",
        R.drawable.nearest_clinic_image,
        "nearest_clinic",
        Topaz
    ),
    ActionCategory(
        "popular_clinic",
        "Terpopuler 4",
        R.drawable.nearest_clinic_image,
        "popular_clinic",
        TrueBlue
    ),
    ActionCategory(
        "nearest_clinic",
        "Terdekat 5",
        R.drawable.nearest_clinic_image,
        "nearest_clinic",
        Topaz
    ),
    ActionCategory(
        "popular_clinic",
        "Terpopuler 6",
        R.drawable.nearest_clinic_image,
        "popular_clinic",
        TrueBlue
    ),
    ActionCategory(
        "nearest_clinic",
        "Terdekat 7",
        R.drawable.nearest_clinic_image,
        "nearest_clinic",
        Topaz
    ),
    ActionCategory(
        "popular_clinic",
        "Terpopuler 8",
        R.drawable.nearest_clinic_image,
        "popular_clinic",
        TrueBlue
    ),
    ActionCategory(
        "nearest_clinic",
        "Terdekat 9",
        R.drawable.nearest_clinic_image,
        "nearest_clinic",
        Topaz
    ),
    ActionCategory(
        "popular_clinic",
        "Terpopuler 10",
        R.drawable.nearest_clinic_image,
        "popular_clinic",
        TrueBlue
    ),
    ActionCategory(
        "nearest_clinic",
        "Terdekat 11",
        R.drawable.nearest_clinic_image,
        "nearest_clinic",
        Topaz
    ),
    ActionCategory(
        "popular_clinic",
        "Terpopuler 12",
        R.drawable.nearest_clinic_image,
        "popular_clinic",
        TrueBlue
    ),
    ActionCategory(
        "nearest_clinic",
        "Terdekat 13",
        R.drawable.nearest_clinic_image,
        "nearest_clinic",
        Topaz
    ),
    ActionCategory(
        "popular_clinic",
        "Terpopuler 14",
        R.drawable.nearest_clinic_image,
        "popular_clinic",
        TrueBlue
    ),
    ActionCategory(
        "nearest_clinic",
        "Terdekat 15",
        R.drawable.nearest_clinic_image,
        "nearest_clinic",
        Topaz
    ),
    ActionCategory(
        "popular_clinic",
        "Terpopuler 16",
        R.drawable.nearest_clinic_image,
        "popular_clinic",
        TrueBlue
    )
)

val petClinicList = listOf<PetClinic>(
    PetClinic(
        "0",
        "Clinic A",
        R.drawable.nearest_clinic_image,
        "01234567891",
        "A Street",
        5.0,
        10,
        37.4219983,
        -122.084
    ),
    PetClinic(
        "1",
        "Clinic B",
        R.drawable.nearest_clinic_image,
        "01234567891",
        "B Street",
        4.5,
        50,
        37.4219983,
        -122.084
    ),
    PetClinic(
        "2",
        "Clinic C",
        R.drawable.nearest_clinic_image,
        "01234567891",
        "C Street",
        4.5,
        50,
        37.4219983,
        -122.084
    )
)
