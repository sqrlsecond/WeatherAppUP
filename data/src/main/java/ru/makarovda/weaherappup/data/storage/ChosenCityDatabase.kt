package ru.makarovda.weaherappup.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.makarovda.weaherappup.data.City

@Database(entities = [City::class], version = 1)
abstract class ChosenCityDatabase: RoomDatabase() {
    abstract fun chosenCitiesDao(): ChosenCitiesDao
}