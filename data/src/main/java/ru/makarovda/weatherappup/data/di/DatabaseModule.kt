package ru.makarovda.weatherappup.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.makarovda.weatherappup.data.storage.ChosenCitiesDao
import ru.makarovda.weatherappup.data.storage.ChosenCityDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun getDatabase(context: Context): ChosenCityDatabase {
        return Room.databaseBuilder(
            context,
            ChosenCityDatabase::class.java, "citiesDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun getChosenCityDao(context: Context): ChosenCitiesDao {
        return getDatabase(context).chosenCitiesDao()
    }
}