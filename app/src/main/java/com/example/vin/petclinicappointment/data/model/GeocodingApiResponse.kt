package com.example.vin.petclinicappointment.data.model

data class GeocodingApiResponse(
    val results: List<GeocodingApiResult>
)
data class GeocodingApiResult(
    val name: String,
    val country: String,
    val country_code: String,
    val state: String,
    val state_code: String,
    val county: String,
    val county_code: String,
    val postcode: String,
    val city: String,
    val street: String,
    val housenumber: String,
    val lon: Double,
    val lat: Double,
    val formatted: String,
    val address_line1: String,
    val address_line2: String,
    val result_type: String,
    val distance: String,
    val rank: Rank,
    val dataSource: DataSource,
    val category: String,
    val place_id: String
)

data class Rank (
    val confidence: Double,
    val confidence_city_level: Double,
    val confidence_street_level: Double,
    val match_type: String
        )

data class DataSource (
    val sourcename: String,
    val attribution: String,
    val licence: String,
    val url: String
        )
