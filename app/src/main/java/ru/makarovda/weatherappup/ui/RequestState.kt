package ru.makarovda.weatherappup.ui

import ru.makarovda.weatherappup.data.City
import ru.makarovda.weatherappup.domain.CityDomain
import ru.makarovda.weatherappup.domain.Weather

sealed class RequestState{
    object InProgress : RequestState()
    class WeatherSuccess(val response: Weather) : RequestState()
    class FindCitiesSuccess(val response: List<CityDomain>): RequestState()

    class ChosenCitiesSuccess(val response: List<CityDomain>): RequestState()

    class Error(val errorMsg: String) : RequestState()
}
