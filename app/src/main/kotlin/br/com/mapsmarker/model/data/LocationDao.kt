package br.com.mapsmarker.model.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import br.com.mapsmarker.model.domain.LocationDTO

@Dao
interface LocationDao {

    @Query("SELECT * FROM LocationDTO WHERE placeId = :placeId")
    fun getLocationById(placeId: String): LiveData<LocationDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationDTO): Long?

    @Delete
    fun deleteLocation(location: LocationDTO): Int

}
