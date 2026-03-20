package com.weather.feature.weather.infra.adapter

import com.weather.core.model.City
import com.weather.core.model.Weather
import com.weather.feature.weather.domain.WeatherRepository
import com.weather.feature.weather.infra.remote.OpenWeatherMapService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class OpenWeatherMapWeatherRepository(
    private val service: OpenWeatherMapService
) : WeatherRepository {
    private val mapper = Mapper()
    override suspend fun getWeather(city: City): Weather =
        coroutineScope {
            val current = async { service.getCurrentWeather(city.name) }
            val forecast = async { service.getForecast(city.name) }
            mapper.mapToWeather(current.await(), forecast.await())
        }
}