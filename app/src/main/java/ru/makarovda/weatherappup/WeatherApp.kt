package ru.makarovda.weatherappup

import android.app.Application
import ru.makarovda.weatherappup.di.AppComponent
import ru.makarovda.weatherappup.di.DaggerAppComponent
import ru.makarovda.weatherappup.di.DatabaseModule

class WeatherApp: Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().databaseModule(DatabaseModule(this)).build()

    }
}