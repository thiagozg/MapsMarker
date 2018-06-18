package br.com.mapsmarker.di.modules

import br.com.mapsmarker.features.home.HomeActivity
import br.com.mapsmarker.features.map.MapsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    internal abstract fun bindMapsActivity(): MapsActivity

}
