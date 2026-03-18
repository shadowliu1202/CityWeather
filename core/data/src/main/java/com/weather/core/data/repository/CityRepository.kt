package com.weather.core.data.repository

import com.weather.core.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun getCities(): Flow<List<City>>
    fun searchCities(query: String): Flow<List<City>>
}
