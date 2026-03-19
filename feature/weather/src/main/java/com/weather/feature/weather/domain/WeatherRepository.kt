package com.weather.feature.weather.domain

import com.weather.core.model.City
import com.weather.core.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(city: City): Flow<Weather>
}
