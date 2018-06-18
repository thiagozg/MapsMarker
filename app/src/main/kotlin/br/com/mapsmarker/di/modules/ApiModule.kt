package br.com.mapsmarker.di.modules

import br.com.mapsmarker.model.api.GoogleMapsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun providesGoogleMapsApi(retrofit: Retrofit) = retrofit.create(GoogleMapsApi::class.java)

}
