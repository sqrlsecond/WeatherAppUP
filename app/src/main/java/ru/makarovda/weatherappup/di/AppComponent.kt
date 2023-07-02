package ru.makarovda.weatherappup.di


import dagger.BindsInstance
import dagger.Component
import ru.makarovda.weatherappup.domain.IRepository
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun repository(repository: IRepository): Builder

        fun build(): AppComponent
    }

    fun getRepository(): IRepository

}