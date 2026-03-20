package com.weather.feature.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.model.City
import com.weather.feature.weather.domain.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()
    fun loadWeather(city: City) {
        _weatherState.value = WeatherUiState.Loading
        viewModelScope.launch {
            try {
                val weather = weatherRepository.getWeather(city)
                _weatherState.value = WeatherUiState.Success(weather)
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }
}
