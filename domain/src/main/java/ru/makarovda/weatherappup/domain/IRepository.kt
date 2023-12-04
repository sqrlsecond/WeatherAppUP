package ru.makarovda.weatherappup.domain

import kotlinx.coroutines.flow.Flow


interface IRepository {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): RequestState

    suspend fun findCities(name: String): RequestState

    suspend fun getChosenCities(): Flow<List<CityDomain>>

    suspend fun addChosenCity(city: CityDomain)

    suspend fun removeChosenCity(city: CityDomain)

    suspend fun getCachedWeather(): RequestState
}