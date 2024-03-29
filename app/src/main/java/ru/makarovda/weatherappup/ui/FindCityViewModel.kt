package ru.makarovda.weatherappup.ui

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
import ru.makarovda.weatherappup.domain.IRepository
import ru.makarovda.weatherappup.domain.RequestState

class FindCityViewModel(private val repository: IRepository): ViewModel() {

    private val _citiesResponseFlow = MutableStateFlow<RequestState>(RequestState.InProgress)
    val citiesResponseFlow: StateFlow<RequestState>
        get() = _citiesResponseFlow

    fun asyncFindCity(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val requestResult = repository.findCities(name)
            _citiesResponseFlow.emit(requestResult)
            }
        }

    fun addChosenCity(city: CityDomain){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChosenCity(city)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val component = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherApp).appComponent
                FindCityViewModel(component.getRepository())
            }
        }
    }
}