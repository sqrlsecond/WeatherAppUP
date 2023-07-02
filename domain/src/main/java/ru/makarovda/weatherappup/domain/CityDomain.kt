package ru.makarovda.weatherappup.domain

data class CityDomain(
    val id: Int,
    val name: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    var isChosen: Boolean
)
