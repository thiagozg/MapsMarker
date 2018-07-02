package br.com.mapsmarker.model.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import br.com.mapsmarker.model.domain.LocationDTO
import br.com.mapsmarker.model.domain.ResultVO
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationDaoTest {

    protected lateinit var locationDatabase: LocationDatabase

    @Before
    fun setUp() {
        locationDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                LocationDatabase::class.java)
                .build()
    }

    @After
    fun tearDown() = locationDatabase.close()
    
    @Test
    fun insertLocationTest() {
        val placeId = "HUE"
        val location = LocationDTO(ResultVO(placeId = placeId), true)

        val returnFromDb = locationDatabase.locationDao().insertLocation(location)
        val locationFromDb = locationDatabase.locationDao().getLocationById(placeId)

        assertEquals(returnFromDb, 1L)
        assertEquals(locationFromDb.placeId, location.placeId)
    }

    @Test
    fun deleteLocationTest() {
        val placeId = "HUE"
        val location = LocationDTO(ResultVO(placeId = placeId), false)

        locationDatabase.locationDao().insertLocation(location)
        var locationFromDb = locationDatabase.locationDao().getLocationById(placeId)
        val returnFromDb = locationDatabase.locationDao().deleteLocation(locationFromDb)

        locationFromDb = locationDatabase.locationDao().getLocationById(placeId)
        assertNull(locationFromDb)
        assertEquals(returnFromDb, 1)
    }

    @Test
    fun getLocationTest() {
        val placeId = "HUE"
        val location = LocationDTO(ResultVO(placeId = placeId), true)

        locationDatabase.locationDao().insertLocation(location)

        val locationFromDb = locationDatabase.locationDao().getLocationById(placeId)
        assertEquals(locationFromDb.placeId, location.placeId)
    }
    
    
}