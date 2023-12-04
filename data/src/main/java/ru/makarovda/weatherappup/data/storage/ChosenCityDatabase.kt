package ru.makarovda.weatherappup.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.makarovda.weatherappup.data.City

@Database(entities = [City::class], version = 1)
abstract class ChosenCityDatabase: RoomDatabase() {
    abstract fun chosenCitiesDao(): ChosenCitiesDao
}