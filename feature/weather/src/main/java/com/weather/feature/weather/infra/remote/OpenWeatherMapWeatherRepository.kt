package com.weather.feature.weather.infra.remote

import com.weather.core.model.Weather
import com.weather.feature.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


internal class OpenWeatherMapWeatherRepository(
    private val service: OpenWeatherMapService
) : WeatherRepository {
    private val mapper = Mapper()
    override fun getWeather(cityId: String): Flow<Weather> = flow {
        val cityName = mapper.cityIdToName(cityId)
        val current = service.getCurrentWeather(cityName)
        val forecast = service.getForecast(cityName)
        emit(mapper.mapToWeather(current, forecast))
    }
}
