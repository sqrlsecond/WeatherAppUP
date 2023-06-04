package ru.makarovda.weatherappup.domain

data class Weather(
    // Информация о городе
    val city: City,

    // Находится ли этот город в избранном
    val isCityChosen: Boolean,

    // Температура в градусах Цельсия
    val temp_c: Float,

    // Состояние (ясно, дождь и т.д.)
    val condition: Int,

    // Ощущаемая температура в градусах Цельсия
    val feelslike_c: Float,

    // Скорость ветра в км/ч
    val wind_kph: Float,

    // Влажность в процентах
    val humidity: Int
)
