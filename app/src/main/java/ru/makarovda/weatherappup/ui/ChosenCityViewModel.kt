package ru.makarovda.weatherappup.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.makarovda.weatherappup.WeatherApp
import ru.makarovda.weatherappup.domain.CityDomain
import ru.makarovda.weatherappup.domain.IRepository
import ru.makarovda.weatherappup.domain.RequestState

class ChosenCityViewModel(private val repository: IRepository): ViewModel() {

    private val _citiesResponseFlow = MutableSharedFlow<List<CityDomain>>()
    val citiesResponseFlow: SharedFlow<List<CityDomain>>
        get() = _citiesResponseFlow

    fun asyncGetChosenCity() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getChosenCities().collect {
                _citiesResponseFlow.emit(it)
            }
        }
    }

    fun removeChosenCity(cityDomain: CityDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeChosenCity(cityDomain)
        }
        asyncGetChosenCity()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val component = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherApp).appComponent
                ChosenCityViewModel(component.getRepository())
            }
        }
    }
}