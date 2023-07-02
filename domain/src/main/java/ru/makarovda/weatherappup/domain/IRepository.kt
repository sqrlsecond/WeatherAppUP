package ru.makarovda.weatherappup.domain

import kotlinx.coroutines.flow.Flow


interface IRepository {

    suspend fun getCurrentWeather(location: String): Flow<Weather>

    suspend fun findCities(name: String): Flow<List<CityDomain>>

    suspend fun getChosenCities(): Flow<List<CityDomain>>

    suspend fun addChosenCity(city: CityDomain)

    suspend fun removeChosenCity(city: CityDomain)

}