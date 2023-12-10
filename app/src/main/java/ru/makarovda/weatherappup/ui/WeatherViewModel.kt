package ru.makarovda.weatherappup.ui

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.makarovda.weatherappup.WeatherApp
import ru.makarovda.weatherappup.domain.CityDomain
import ru.makarovda.weatherappup.domain.RequestState

import ru.makarovda.weatherappup.domain.IRepository

class WeatherViewModel(private val repository: IRepository): ViewModel() {


    private val _weatherResponseFlow = MutableStateFlow<RequestState>(RequestState.InProgress)

    private var currentLocation: LocationCoords = LocationCoords(0.0, 0.0)
    val weatherResponseFlow: StateFlow<RequestState>
        get() = _weatherResponseFlow

    fun asyncRequestWeather(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            currentLocation.latitude = location.latitude
            currentLocation.longitude = location.longitude
            val weatherResult = repository.getCurrentWeather(currentLocation.latitude, currentLocation.longitude)
            _weatherResponseFlow.emit(weatherResult)
        }
    }

    fun asyncRequestWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            currentLocation.latitude = latitude
            currentLocation.longitude = longitude
            val weatherResult = repository.getCurrentWeather(currentLocation.latitude, currentLocation.longitude)
            _weatherResponseFlow.emit(weatherResult)
        }
    }

    fun asyncRequestWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherResult = repository.getCurrentWeather(currentLocation.latitude, currentLocation.longitude)
            _weatherResponseFlow.emit(weatherResult)
        }
    }

    fun setCoordinates(latitude: Double, longitude: Double) {
        currentLocation.latitude = latitude
        currentLocation.longitude = longitude
    }

    fun setCoordinates(location: Location) {
        currentLocation.latitude = location.latitude
        currentLocation.longitude = location.longitude
        asyncRequestWeather()
    }

    fun asyncAddChosenCity(city: CityDomain){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChosenCity(city)
        }
    }

    fun asyncDeleteChosenCity(city: CityDomain){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeChosenCity(city)
        }
    }

    fun getCachedWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherResult = repository.getCachedWeather()
            if(weatherResult is RequestState.WeatherSuccess) {
                _weatherResponseFlow.emit(weatherResult)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val component = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherApp).appComponent
                WeatherViewModel(component.getRepository())
            }
        }
    }

    private data class LocationCoords(
        var latitude: Double,
        var longitude: Double
    )
}