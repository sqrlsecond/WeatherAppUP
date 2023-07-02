package ru.makarovda.weaherappup.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.makarovda.weaherappup.data.City

interface IWeatherService {

    @GET("./current.json?key=$apiKeyStr")
    suspend fun getCurrentWeather(@Query("q", encoded=true) location: String): Response<WeatherResponse>

    @GET("./search.json?key=$apiKeyStr")
    suspend fun getCities(@Query("q", encoded=true) cityName: String): Response<List<City>>
}