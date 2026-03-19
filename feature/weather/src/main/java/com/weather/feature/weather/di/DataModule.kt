package com.weather.feature.weather.di

import com.weather.feature.weather.BuildConfig
import com.weather.feature.weather.domain.WeatherRepository
import com.weather.feature.weather.infra.remote.OpenWeatherMapService
import com.weather.feature.weather.infra.remote.OpenWeatherMapWeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true  // tolerant of extra fields the app doesn't use
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = if (BuildConfig.DEBUG) LogLevel.BODY else LogLevel.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOpenWeatherMapService(httpClient: HttpClient): OpenWeatherMapService =
        OpenWeatherMapService(
            httpClient = httpClient,
            apiKey = BuildConfig.OPENWEATHER_API_KEY
        )

    @Provides
    @Singleton
    fun provideWeatherRepository(
        service: OpenWeatherMapService
    ): WeatherRepository = OpenWeatherMapWeatherRepository(service)
}
