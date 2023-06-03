package ru.makarovda.weatherappup.domain

import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun getCurrentWeather(location: String): Flow<WeatherResponse>

}