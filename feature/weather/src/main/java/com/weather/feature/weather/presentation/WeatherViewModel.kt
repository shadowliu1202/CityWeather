package com.weather.feature.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.model.City
import com.weather.feature.weather.domain.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedCity = MutableStateFlow(City.Default)

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    init {
        loadWeather(_selectedCity.value)
    }

    fun loadWeather(city: City) {
        _selectedCity.value = city
        _weatherState.value = WeatherUiState.Loading
        viewModelScope.launch {
            weatherRepository.getWeather(city)
                .catch { e ->
                    _weatherState.value = WeatherUiState.Error(
                        e.message ?: "Unknown error occurred"
                    )
                }
                .collect { weather ->
                    _weatherState.value = WeatherUiState.Success(weather)
                }
        }
    }
}
