package ru.makarovda.weatherappup.data

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.makarovda.weatherappup.data.network.IWeatherService
import ru.makarovda.weatherappup.data.storage.ChosenCitiesDao
import ru.makarovda.weatherappup.domain.CityDomain
import ru.makarovda.weatherappup.domain.IRepository
import ru.makarovda.weatherappup.domain.RequestState
import ru.makarovda.weatherappup.domain.Weather
import java.io.File
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkService: IWeatherService,
    private val chosenCitiesDao: ChosenCitiesDao,
    private val context: Context
): IRepository {

    private val _chosenCitiesFlow = MutableSharedFlow<List<CityDomain>>(replay = 1)

    private val weatherFilename = "weatherData.json"

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): RequestState {
        try {
            val location = String.format("%.2f,%.2f", latitude, longitude)
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
                    cacheWeather(weather)
                    return RequestState.WeatherSuccess(weather)
                }
            }
        }
        catch(e: Exception) {
            return RequestState.Error("Connection problem")
        }
        
        return RequestState.Error("Connection problem")
    }

    override suspend fun findCities(name: String): RequestState
    {
        val responseResult =
        try {
            val response = networkService.getCities(name)
            if (response.isSuccessful) {
                response.body()?.let {cities->
                    val citiesFromDb = chosenCitiesDao.getChosenCities()
                    val citiesDomain = ArrayList<CityDomain>(cities.size)
                    cities.forEach {
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
                    RequestState.CitiesSuccess(citiesDomain)
                }
            } else {
                RequestState.Error(response.errorBody().toString())
            }
        }
        catch(e: Exception) {
            RequestState.Error(e.toString())
        }
        return responseResult!!
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

    private fun cacheWeather(weather: Weather) {
        val jsonConverter = Gson()
        val dataToWrite = jsonConverter.toJson(weather)
        File(context.cacheDir, weatherFilename).writeText(dataToWrite)
    }

    private fun readWeatherFromCache(): Weather? {
        val file = File(context.cacheDir, weatherFilename)
        if (file.canRead()) {
            val jsonConverter = Gson()
            return jsonConverter.fromJson(file.reader(), Weather::class.java)
        }
        return null
    }

    override suspend fun getCachedWeather(): RequestState {
        val weatherLast = readWeatherFromCache()
        if (weatherLast == null){
            return RequestState.Error("")
        }
        return RequestState.WeatherSuccess(weatherLast)
    }
}
