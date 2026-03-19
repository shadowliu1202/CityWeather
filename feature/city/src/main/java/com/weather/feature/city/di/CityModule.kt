package com.weather.feature.city.di

import com.weather.feature.city.domain.CityRepository
import com.weather.feature.city.infra.FakeCityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CityModule {
    @Binds
    @Singleton
    abstract fun bindCityRepository(
        fakeCityRepository: FakeCityRepository
    ): CityRepository
}
