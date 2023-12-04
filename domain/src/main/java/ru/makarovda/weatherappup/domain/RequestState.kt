package ru.makarovda.weatherappup.domain

sealed class RequestState{

    object InProgress : RequestState()

    class WeatherSuccess(val response: Weather) : RequestState()

    class CitiesSuccess(val response: List<CityDomain>): RequestState()

    class Error(val errorMsg: String) : RequestState()
}
