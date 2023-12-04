package ru.makarovda.weatherappup

import android.app.Application
import ru.makarovda.weatherappup.data.di.DaggerDataComponent
import ru.makarovda.weatherappup.di.AppComponent
import ru.makarovda.weatherappup.di.DaggerAppComponent

class WeatherApp: Application() {

    lateinit var appComponent: AppComponent
        private set



    override fun onCreate() {
        super.onCreate()
        val dataComponent = DaggerDataComponent.builder().context(this).build()
        appComponent = DaggerAppComponent.builder().repository(dataComponent.getRepository()).build()

    }
}