package br.com.mapsmarker.features.home

import br.com.mapsmarker.model.repository.GoogleMapsRepository
import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class HomeUseCaseTest {

    val repository = mock<GoogleMapsRepository>()
    val useCase: HomeUseCase by lazy { spy(HomeUseCase(repository)) }

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