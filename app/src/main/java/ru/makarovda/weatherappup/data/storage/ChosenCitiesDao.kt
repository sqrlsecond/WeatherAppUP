package ru.makarovda.weatherappup.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.makarovda.weatherappup.data.City


@Dao
interface ChosenCitiesDao {

    @Query("SELECT * FROM cities")
    fun getChosenCities(): List<City>

    @Insert
    fun addCity(city: City)

    @Delete
    fun removeCity(city: City)

    @Query("SELECT EXISTS(SELECT * FROM cities WHERE id = :cityId)")
    fun isCityChosen(cityId: Int): Boolean
}