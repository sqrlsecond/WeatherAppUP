package ru.makarovda.weatherappup.domain

sealed class RequestState{

    object InProgress : RequestState()

    class WeatherSuccess(val response: Weather) : RequestState()

    class CitiesSuccess(val response: List<CityDomain>): RequestState()

    class Error(val errorCode: ErrorCode = ErrorCode.OTHER) : RequestState()
    {
        enum class ErrorCode {
            NO_CONNECTION,
            FILE_NOT_FOUND,
            SERVER_ERROR,
            OTHER,
        }
    }
}
