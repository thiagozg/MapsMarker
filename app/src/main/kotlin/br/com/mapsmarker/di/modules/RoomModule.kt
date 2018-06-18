package br.com.mapsmarker.di.modules

import android.arch.persistence.room.Room
import br.com.mapsmarker.CustomApplication
import br.com.mapsmarker.model.data.LocationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideLocationDatabase(application: CustomApplication) =
            Room.databaseBuilder(
                    application,
                    LocationDatabase::class.java,
                    "Location.db"
            ).build()

    @Provides
    @Singleton
    fun provideLocationDao(database: LocationDatabase) = database.locationDao()

}
