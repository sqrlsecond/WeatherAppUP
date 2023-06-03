package ru.makarovda.weatherappup.data

import androidx.room.util.newStringBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.makarovda.weatherappup.data.network.IWeatherService
import ru.makarovda.weatherappup.domain.City
import ru.makarovda.weatherappup.domain.IRepository
import ru.makarovda.weatherappup.domain.WeatherResponse
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkService: IWeatherService
): IRepository {

    private val _weatherFlow = MutableSharedFlow<WeatherResponse>(replay = 1)

    private val _citiesFlow = MutableSharedFlow<List<City>>(replay = 1)

    override suspend fun getCurrentWeather(location: String): Flow<WeatherResponse>
    {
        val response = networkService.getCurrentWeather(location)
        if(response.isSuccessful){
            response.body()?.let {
                _weatherFlow.emit(it)
            }
        }
        return _weatherFlow
    }

    override suspend fun findCities(name: String): Flow<List<City>>
    {
        val response = networkService.getCities(name)
        if(response.isSuccessful){
            response.body()?.let {
                _citiesFlow.emit(it)
            }
        }
        return _citiesFlow
    }
}