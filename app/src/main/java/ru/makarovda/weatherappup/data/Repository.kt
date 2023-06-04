package ru.makarovda.weatherappup.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.makarovda.weatherappup.data.network.IWeatherService
import ru.makarovda.weatherappup.data.storage.ChosenCitiesDao
import ru.makarovda.weatherappup.domain.City
import ru.makarovda.weatherappup.domain.IRepository
import ru.makarovda.weatherappup.data.network.WeatherResponse
import ru.makarovda.weatherappup.domain.Weather
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkService: IWeatherService,
    private val chosenCitiesDao: ChosenCitiesDao
): IRepository {

    private val _weatherFlow = MutableSharedFlow<Weather>(replay = 1)

    private val _citiesFlow = MutableSharedFlow<List<City>>(replay = 1)

    private val _chosenCitiesFlow = MutableSharedFlow<List<City>>(replay = 1)

    override suspend fun getCurrentWeather(location: String): Flow<Weather>
    {
        val responseWeather = networkService.getCurrentWeather(location)
        val responseCity = getCity(location)
        if(responseWeather.isSuccessful && (responseCity != null)){

            responseWeather.body()?.let {
                val weather = Weather(
                    responseCity,
                    chosenCitiesDao.isCityChosen(responseCity.id),
                    it.current.temp_c,
                    it.current.condition.code,
                    it.current.feelslike_c,
                    it.current.wind_kph,
                    it.current.humidity
                )
                _weatherFlow.emit(weather)
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

    override suspend fun getChosenCities(): Flow<List<City>> {
        _chosenCitiesFlow.emit(chosenCitiesDao.getChosenCities())
        return _chosenCitiesFlow
    }

    override suspend fun addChosenCity(city: City) {
        chosenCitiesDao.addCity(city)
        _chosenCitiesFlow.emit(chosenCitiesDao.getChosenCities())
    }

    override suspend fun removeChosenCity(city: City) {
        chosenCitiesDao.removeCity(city)
        _chosenCitiesFlow.emit(chosenCitiesDao.getChosenCities())
    }

    private suspend fun getCity(location: String): City?{
        //на сервер название города и координаты передаются одинаковыми параметрами GET запроса
        val response = networkService.getCities(location)
        if(response.isSuccessful){
            val respBody = response.body()
            if(respBody != null) {
                if (respBody.isNotEmpty()) return respBody[0]
            }
        }
        return null
    }
}