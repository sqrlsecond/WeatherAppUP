package ru.makarovda.weatherappup.ui

import ru.makarovda.weatherappup.domain.WeatherResponse

sealed class RequestState{
    object InProgress : RequestState()
    class WeatherSuccess(val response: WeatherResponse) : RequestState()
    class Error(val errorMsg: String) : RequestState()
}
