package br.com.mapsmarker.features.home

import br.com.mapsmarker.model.repository.GoogleMapsRepository
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HomeUseCaseTest {

    @Mock
    lateinit var repository: GoogleMapsRepository

    lateinit var useCase: HomeUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = spy(HomeUseCase(repository))
    }

    @Test(expected = KotlinNullPointerException::class)
    fun testRequestSearchByQuery_noQuery() {
        val query: String? = null
        useCase.requestSearchByQuery(query!!)
        verify(repository, never()).searchByQuery(query)
    }

    @Test
    fun testRequestSearchByQuery_shouldRequestOneTime() {
        useCase.requestSearchByQuery(anyString())
        verify(repository, times(1)).searchByQuery(anyString())
    }

    @Test
    fun testRequestSearchByQuery_shoudRequestThreeTimes() {
        repeat(3) {
            useCase.requestSearchByQuery(anyString())
        }
        verify(repository, times(3)).searchByQuery(anyString())
    }

}