package com.weather.feature.weather.infra.remote

import com.weather.core.model.City
import com.weather.core.model.Weather
import com.weather.feature.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


internal class OpenWeatherMapWeatherRepository(
    private val service: OpenWeatherMapService
) : WeatherRepository {
    private val mapper = Mapper()
    override fun getWeather(city: City): Flow<Weather> = flow {
        val current = service.getCurrentWeather(city.name)
        val forecast = service.getForecast(city.name)
        emit(mapper.mapToWeather(current, forecast))
    }
}
