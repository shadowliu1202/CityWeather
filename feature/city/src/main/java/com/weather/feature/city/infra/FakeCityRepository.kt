package com.weather.feature.city.infra

import com.weather.core.model.City
import com.weather.feature.city.domain.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class FakeCityRepository @Inject constructor() : CityRepository {

    private val cities = listOf(
        City(
            id = "taipei",
            name = "Taipei",
            country = "Taiwan",
            latitude = 25.0330,
            longitude = 121.5654
        ),
        City(
            id = "tokyo",
            name = "Tokyo",
            country = "Japan",
            latitude = 35.6762,
            longitude = 139.6503
        ),
        City(
            id = "new_york",
            name = "New York",
            country = "United States",
            latitude = 40.7128,
            longitude = -74.0060
        ),
        City(
            id = "london",
            name = "London",
            country = "United Kingdom",
            latitude = 51.5074,
            longitude = -0.1278
        ),
        City(
            id = "paris",
            name = "Paris",
            country = "France",
            latitude = 48.8566,
            longitude = 2.3522
        ),
        City(
            id = "sydney",
            name = "Sydney",
            country = "Australia",
            latitude = -33.8688,
            longitude = 151.2093
        ),
        City(
            id = "seoul",
            name = "Seoul",
            country = "South Korea",
            latitude = 37.5665,
            longitude = 126.9780
        ),
        City(
            id = "singapore",
            name = "Singapore",
            country = "Singapore",
            latitude = 1.3521,
            longitude = 103.8198
        )
    )

    override fun getCities(): Flow<List<City>> = flowOf(cities)

    override fun searchCities(query: String): Flow<List<City>> {
        if (query.isBlank()) return flowOf(cities)
        val filtered = cities.filter { city ->
            city.name.contains(query, ignoreCase = true) ||
                    city.country.contains(query, ignoreCase = true)
        }
        return flowOf(filtered)
    }
}