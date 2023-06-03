package ru.makarovda.weatherappup.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.makarovda.weatherappup.data.network.IWeatherService
import javax.inject.Singleton


const val baseUrl = "https://api.weatherapi.com/v1/"

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun getWeatherService(): IWeatherService {

        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        return retrofit.create(IWeatherService::class.java)
    }
}