package ru.makarovda.weatherappup.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.makarovda.weatherappup.data.storage.ChosenCitiesDao
import ru.makarovda.weatherappup.data.storage.ChosenCityDatabase
import javax.inject.Singleton

@Module
class DatabaseModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    fun getDatabase(): ChosenCityDatabase {
        return Room.databaseBuilder(
            applicationContext,
            ChosenCityDatabase::class.java, "citiesDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun getChosenCityDao(): ChosenCitiesDao {
        return getDatabase().chosenCitiesDao()
    }
}