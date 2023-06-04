package ru.makarovda.weatherappup.ui

import ru.makarovda.weatherappup.domain.City
import ru.makarovda.weatherappup.domain.Weather

sealed class RequestState{
    object InProgress : RequestState()
    class WeatherSuccess(val response: Weather) : RequestState()
    class FindCitiesSuccess(val response: List<City>): RequestState()

    class ChosenCitiesSuccess(val response: List<City>): RequestState()

    class Error(val errorMsg: String) : RequestState()
}
