package ru.makarovda.weaherappup.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
    val lat: Float,
    val lon: Float
    )

