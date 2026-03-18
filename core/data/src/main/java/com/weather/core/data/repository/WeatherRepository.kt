package com.weather.core.data.repository

import com.weather.core.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(cityId: String): Flow<Weather>
}
