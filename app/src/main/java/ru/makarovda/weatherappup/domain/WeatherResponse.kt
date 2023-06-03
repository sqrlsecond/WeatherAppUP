package ru.makarovda.weatherappup.domain

data class WeatherResponse(
    val location: Location,
    val current: Current
) {
    data class Location (
        val name: String,
        val country: String
        )

    data class Current(
        // Температура в градусах Цельсия
        val temp_c: Float,

        // Состояние (ясно, дождь и т.д.)
        val condition: Condition,

        // Ощущаемая температура в градусах Цельсия
        val feelslike_c: Float,

        // Скорость ветра в км/ч
        val wind_kph: Float,

        // Влажность в процентах
        val humidity: Int

        ) {
        data class Condition(
            val code: Int
        )

    }
}
