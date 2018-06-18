package br.com.mapsmarker.di

import br.com.mapsmarker.CustomApplication
import br.com.mapsmarker.di.modules.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AndroidModule::class,
    NetModule::class,
    RetrofitModule::class,
    ApiModule::class,
    RoomModule::class,
    BuildersModule::class]
)
interface AppComponent : AndroidInjector<CustomApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<CustomApplication>()

}
