package br.com.mapsmarker.model.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import br.com.mapsmarker.model.domain.LocationDTO

@Database(entities = arrayOf(LocationDTO::class), version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDao() : LocationDao

}
