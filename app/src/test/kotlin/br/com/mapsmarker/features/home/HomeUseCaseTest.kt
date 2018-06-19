package br.com.mapsmarker.features.home

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.mapsmarker.mock
import br.com.mapsmarker.model.domain.SearchResponseVO
import br.com.mapsmarker.model.repository.GoogleMapsRepository
import br.com.mapsmarker.whenever
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class HomeUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val repository = mock<GoogleMapsRepository>()

    val useCase by lazy { HomeUseCase(repository) }

    @Test
    fun testSearchByQuery_getSearchResponse_singleCompleted() {
        whenever(repository.searchByQuery(anyString()))
                .thenReturn(Single.just(SearchResponseVO()))

        useCase.requestSearchByQuery("Springfield")
                .test()
                .assertComplete()
    }

    @Test
    fun testSearchByQuery_getSearchResponse_singleError() {
        val response = Throwable("Error response")
        whenever(repository.searchByQuery(anyString()))
                .thenReturn(Single.error(response))

        useCase.requestSearchByQuery("Springfield")
                .test()
                .assertError(response)
    }

}