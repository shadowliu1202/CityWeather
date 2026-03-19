package com.weather.feature.weather.domain

import com.weather.core.model.City
import com.weather.core.model.Weather

interface WeatherRepository {
    suspend fun getWeather(city: City): Weather
}
