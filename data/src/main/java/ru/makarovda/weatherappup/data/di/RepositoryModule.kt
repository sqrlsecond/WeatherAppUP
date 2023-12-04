package ru.makarovda.weatherappup.data.di

import dagger.Binds
import dagger.Module
import ru.makarovda.weatherappup.data.Repository
import ru.makarovda.weatherappup.domain.IRepository
import javax.inject.Singleton


@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun getRepository(repository: Repository): IRepository
}