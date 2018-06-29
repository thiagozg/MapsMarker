package br.com.mapsmarker.features.maps

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import br.com.mapsmarker.features.map.MapsUseCase
import br.com.mapsmarker.features.map.MapsViewModel
import br.com.mapsmarker.model.domain.LatLngBO
import br.com.mapsmarker.model.domain.LocationDTO
import br.com.mapsmarker.model.domain.ResultVO
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor

class MapsViewModelTest {

    @get:Rule var archRule = InstantTaskExecutorRule()

    @get:Rule var schedulersRule = RxSchedulerRule()

    val useCase = mock<MapsUseCase>()
    val viewModel by lazy { MapsViewModel(useCase) }
    val observerState = mock<Observer<LocationDTO>>()

    @Test
    fun testGetLocationStored_shoudHasLocations() {
        // Arrange
        val placeId = "HUE"
        val mockLocation = mock<LocationDTO>()
        val mockResult = ResultVO(placeId = placeId)
        whenever(useCase.searchLocation(placeId))
                .thenReturn(mockLocation)

        // Act
        viewModel.getLocationData().observeForever(observerState)
        viewModel.getLocationStored(mockResult)
        viewModel.getLocationData().removeObserver(observerState)

        // Assert
        val argumentCaptor = ArgumentCaptor.forClass(LocationDTO::class.java)
        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (locationStored) = allValues
            assertThat(locationStored, instanceOf(LocationDTO::class.java))
            assertEquals(mockLocation, locationStored)
        }
    }

    @Test
    fun testGetLocationStored_shoudntHasLocations() {
        val mockResult = ResultVO(placeId = "HUE")

        viewModel.getLocationData().observeForever(observerState)
        viewModel.getLocationStored(mockResult)
        viewModel.getLocationData().removeObserver(observerState)

        val argumentCaptor = ArgumentCaptor.forClass(LocationDTO::class.java)
        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (locationStored) = allValues
            assertNull(locationStored)
        }
    }

    @Ignore
    @Test
    fun testStoreLocation_shoudSaveLocation() {
        // TODO
    }

    @Ignore
    @Test
    fun testStoreLocation_shoudDeleteLocation() {
        // TODO
    }

    @Test
    fun testGetLatLngAverage_shouldReturnAverage() {
        val maxPosition = LatLngBO(10.0, 20.0)
        val minPosition = LatLngBO(20.0, 30.0)
        val avgPosition = viewModel.getLatLngAverage(maxPosition, minPosition)
        assertEquals(15.0, avgPosition.latitude)
        assertEquals(25.0, avgPosition.longitude)
    }

    @Test
    fun testGetClosestToCriterion() {
        var closest = viewModel.getClosest(10.0, 20.0, 9.0)
        assertEquals(10.0, closest)

        closest = viewModel.getClosest(10.0, 20.0, 21.0)
        assertEquals(20.0, closest)

        closest = viewModel.getClosest(-3.0, 20.0, -21.0)
        assertEquals(-3.0, closest)

        closest = viewModel.getClosest(-88.0, -55.0, -60.0)
        assertEquals(-55.0, closest)
    }

}