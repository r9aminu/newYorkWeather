package com.example.newyorkweather.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherData(
        @Query("q") query: String,
        @Query("appid") apiKey: String
    ): WeatherData
}
