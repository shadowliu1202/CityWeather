package com.weather.feature.weather.infra.remote

import com.weather.feature.weather.infra.remote.dto.CurrentWeatherDto
import com.weather.feature.weather.infra.remote.dto.ForecastResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class OpenWeatherMapService(
    private val httpClient: HttpClient,
    private val apiKey: String
) {
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5"
    }

    /**
     * - Free -
     * Endpoints used:
     *  - GET /data/2.5/weather  → current weather for a city
     */
    suspend fun getCurrentWeather(cityName: String): CurrentWeatherDto =
        httpClient.get("$BASE_URL/weather") {
            parameter("q", cityName)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "en")
        }.body()

    /**
     * - Free -
     * Endpoints used:
     *   - GET /data/2.5/forecast → 5-day / 3-hour forecast (up to 40 steps)
     */
    suspend fun getForecast(cityName: String, cnt: Int = 40): ForecastResponseDto =
        httpClient.get("$BASE_URL/forecast") {
            parameter("q", cityName)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "en")
            parameter("cnt", cnt)
        }.body()
}
