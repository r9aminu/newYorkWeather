package com.example.newyorkweather.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherData(
    @Json(name = "name")
    val cityName: String,
    @Json(name = "weather")
    val weather: List<Weather>,
    @Json(name = "main")
    val main: Main
)