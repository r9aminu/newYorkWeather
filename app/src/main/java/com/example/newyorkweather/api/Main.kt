package com.example.newyorkweather.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Main(@Json(name = "temp") val temp: Double,
                @Json(name = "humidity") val humidity: Int)