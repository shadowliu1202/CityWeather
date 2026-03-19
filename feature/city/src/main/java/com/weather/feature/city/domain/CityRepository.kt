package com.weather.feature.city.domain

import com.weather.core.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun getCities(): Flow<List<City>>
    fun searchCities(query: String): Flow<List<City>>
}
