package com.weather.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.core.data.repository.WeatherRepository
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

    private val _selectedCityId = MutableStateFlow(
        savedStateHandle.get<String>("cityId") ?: "taipei"
    )
    val selectedCityId: StateFlow<String> = _selectedCityId.asStateFlow()

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    init {
        loadWeather(_selectedCityId.value)
    }

    fun loadWeather(cityId: String) {
        _selectedCityId.value = cityId
        _weatherState.value = WeatherUiState.Loading
        viewModelScope.launch {
            weatherRepository.getWeather(cityId)
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
