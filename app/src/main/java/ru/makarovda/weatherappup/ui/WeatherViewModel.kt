package ru.makarovda.weatherappup.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.makarovda.weatherappup.WeatherApp

import ru.makarovda.weatherappup.domain.IRepository

class WeatherViewModel(private val repository: IRepository): ViewModel() {


    private val _weatherResponseFlow = MutableStateFlow<RequestState>(RequestState.InProgress)
    val weatherResponseFlow: StateFlow<RequestState>
        get() = _weatherResponseFlow

    fun asyncRequestWeather(location: String) {
        viewModelScope.launch(Dispatchers.IO) {
             repository.getCurrentWeather(location).collect {
                 _weatherResponseFlow.emit(RequestState.WeatherSuccess(it))
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
}