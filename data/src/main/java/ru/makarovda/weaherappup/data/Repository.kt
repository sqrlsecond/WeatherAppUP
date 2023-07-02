package ru.makarovda.weaherappup.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.makarovda.weaherappup.data.network.IWeatherService
import ru.makarovda.weaherappup.data.storage.ChosenCitiesDao
import ru.makarovda.weatherappup.domain.CityDomain
import ru.makarovda.weatherappup.domain.IRepository
import ru.makarovda.weatherappup.domain.Weather
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkService: IWeatherService,
    private val chosenCitiesDao: ChosenCitiesDao
): IRepository {

    private val _weatherFlow = MutableSharedFlow<Weather>(replay = 1)

    private val _citiesFlow = MutableSharedFlow<List<CityDomain>>(replay = 1)

    private val _chosenCitiesFlow = MutableSharedFlow<List<CityDomain>>(replay = 1)

    override suspend fun getCurrentWeather(location: String): Flow<Weather>
    {
        val responseWeather = networkService.getCurrentWeather(location)
        val responseCity = getCity(location)
        if(responseWeather.isSuccessful && (responseCity != null)){

            responseWeather.body()?.let {
                val weather = Weather(
                    CityDomain(
                        responseCity.id,
                        responseCity.name,
                        responseCity.country,
                        responseCity.lat,
                        responseCity.lon,
                        chosenCitiesDao.isCityChosen(responseCity.id)
                    ),
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

    override suspend fun findCities(name: String): Flow<List<CityDomain>>
    {
        val response = networkService.getCities(name)
        if(response.isSuccessful){
            response.body()?.let {
                val citiesFromDb = chosenCitiesDao.getChosenCities()
                val citiesDomain = ArrayList<CityDomain>(it.size)
                it.forEach {
                    citiesDomain.add(
                        CityDomain(
                            it.id,
                            it.name,
                            it.country,
                            it.lat,
                            it.lon,
                            citiesFromDb.contains(it)
                        )
                    )
                }
                _citiesFlow.emit(citiesDomain)
            }
        }
        return _citiesFlow
    }

    override suspend fun getChosenCities(): Flow<List<CityDomain>> {
        val cities = chosenCitiesDao.getChosenCities()
        val citiesDomain = ArrayList<CityDomain>(cities.size)

        cities.forEach {
            citiesDomain.add(
                CityDomain(
                    it.id,
                    it.name,
                    it.country,
                    it.lat,
                    it.lon,
                    true
                )
            )
        }

        _chosenCitiesFlow.emit(citiesDomain)
        return _chosenCitiesFlow
    }

    override suspend fun addChosenCity(city: CityDomain) {
        val cityEntity = City(
            city.id,
            city.name,
            city.country,
            city.lat,
            city.lon,
        )
        chosenCitiesDao.addCity(cityEntity)
        getChosenCities()
    }

    override suspend fun removeChosenCity(city: CityDomain) {
        val cityEntity = City(
            city.id,
            city.name,
            city.country,
            city.lat,
            city.lon,
        )
        chosenCitiesDao.removeCity(cityEntity)
        getChosenCities()
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