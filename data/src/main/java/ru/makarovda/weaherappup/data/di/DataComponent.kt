package ru.makarovda.weaherappup.data.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.makarovda.weatherappup.domain.IRepository
import javax.inject.Singleton


@Singleton
@Component(modules=[NetworkModule::class, DatabaseModule::class, RepositoryModule::class])
interface DataComponent {

    fun getRepository(): IRepository


    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): DataComponent

    }

}

