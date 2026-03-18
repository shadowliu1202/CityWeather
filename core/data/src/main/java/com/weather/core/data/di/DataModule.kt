package com.weather.core.data.di

import com.weather.core.data.repository.CityRepository
import com.weather.core.data.repository.FakeCityRepository
import com.weather.core.data.repository.FakeWeatherRepository
import com.weather.core.data.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        fakeWeatherRepository: FakeWeatherRepository
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindCityRepository(
        fakeCityRepository: FakeCityRepository
    ): CityRepository
}
