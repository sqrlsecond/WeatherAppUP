package ru.makarovda.weatherappup.di

import dagger.Component
import ru.makarovda.weatherappup.data.Repository
import ru.makarovda.weatherappup.domain.IRepository
import javax.inject.Singleton

@Singleton
@Component(modules=[NetworkModule::class, DatabaseModule::class])
interface AppComponent {

    fun getRepository(): Repository
}