package com.example.newyorkweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.newyorkweather.api.WeatherApi
import com.example.newyorkweather.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWeatherData()

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    // Call your function here
                    getWeatherData()
                }

            }
        }, 0, 60000)

    }
    private fun getWeatherData()
    {
        binding.txtCityName.text = "City: Loading..."
        binding.txtTemprature.text = "Temperature: Loading..."
        binding.txtWeather.text = "Weather: Loading..."
        binding.txtWeatherStatus.text = "Description: Loading..."
        binding.txtHumidity.text = "Humidity: Loading..."
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)
        lifecycleScope.launch {
            try {
                val weatherData = withContext(Dispatchers.IO) {
                    weatherApi.getWeatherData("New York", "cdf8c4c6a0775eddb50cdcbbc49d6c64")
                }
                if (weatherData != null) {
                    binding.txtCityName.text = "City: "+weatherData.cityName
                    binding.txtTemprature.text = "Temperature: "+convertKalvinToCelsius(weatherData.main.temp).toString()+"\u00B0C"
                    binding.txtWeather.text = "Weather: "+weatherData.weather.get(0).main
                    binding.txtWeatherStatus.text = "Description: "+weatherData.weather.get(0).description
                    binding.txtHumidity.text = "Humidity: "+weatherData.main.humidity

//                    Toast.makeText(this@MainActivity, "Data " + weatherData, Toast.LENGTH_SHORT)
//                        .show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Issue in Api or Internet Issue",
                        Toast.LENGTH_SHORT
                    ).show()
                }

//                updateUi(weatherData)
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Invalid Request",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    fun convertKalvinToCelsius(kalvin: Double): Int {
        return (kalvin-273.15).toInt()
    }
}