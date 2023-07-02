package ru.makarovda.weaherappup.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.makarovda.weaherappup.data.storage.ChosenCitiesDao
import ru.makarovda.weaherappup.data.storage.ChosenCityDatabase
import javax.inject.Inject
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