package br.com.mapsmarker.features.maps

import br.com.mapsmarker.features.map.MapsUseCase
import br.com.mapsmarker.model.data.LocationDao
import br.com.mapsmarker.model.domain.LocationDTO
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertNull
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class MapsUseCaseTest {

    val dao = mock<LocationDao>()
    val useCase = mock<MapsUseCase>()

    @Test
    fun testSearchLocation_shouldHasLocation() {
        val mockLocation = mock<LocationDTO>()
        doReturn(mockLocation)
                .whenever(dao)
                .getLocationById(anyString())

        useCase.searchLocation(anyString())

        verify(useCase, times(1))
                .searchLocation(anyString())
    }

    @Test
    fun testSearchLocation_shouldntHasLocation() {
        val mockLocation = mock<LocationDTO>()
        doReturn(mockLocation)
                .whenever(dao)
                .getLocationById("Springfield")

        useCase.searchLocation(anyString())

        val locationSearched = verify(useCase, times(1))
                .searchLocation(anyString())
        assertNull(locationSearched)
    }

    @Test
    fun testInsertLocation() {
        val mockLocation = mock<LocationDTO>()
        doReturn(1L)
                .whenever(dao)
                .insertLocation(mockLocation)

        useCase.insetLocation(mockLocation)

        verify(useCase, times(1))
                .insetLocation(mockLocation)
    }

    @Test
    fun testDeleteLocation() {
        val mockLocation = mock<LocationDTO>()
        doReturn(1)
                .whenever(dao)
                .deleteLocation(mockLocation)

        useCase.deleteLocation(mockLocation)

        verify(useCase, times(1))
                .deleteLocation(mockLocation)
    }

}