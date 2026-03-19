package com.weather.feature.city.presentation

import com.weather.core.model.City

sealed interface CityUiState {
    data object Loading : CityUiState
    data class Success(val cities: List<City>) : CityUiState
    data class Error(val message: String) : CityUiState
}
