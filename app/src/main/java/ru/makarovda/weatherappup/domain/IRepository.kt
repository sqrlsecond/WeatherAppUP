package ru.makarovda.weatherappup.domain

import kotlinx.coroutines.flow.Flow
import ru.makarovda.weatherappup.data.network.WeatherResponse

interface IRepository {

    suspend fun getCurrentWeather(location: String): Flow<Weather>

    suspend fun findCities(name: String): Flow<List<City>>

    suspend fun getChosenCities(): Flow<List<City>>

    suspend fun addChosenCity(city: City)

    suspend fun removeChosenCity(city: City)

}