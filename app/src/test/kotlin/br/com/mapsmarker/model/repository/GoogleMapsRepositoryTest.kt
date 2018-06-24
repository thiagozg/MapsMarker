package br.com.mapsmarker.model.repository

import br.com.mapsmarker.model.domain.SearchResponseVO
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class GoogleMapsRepositoryTest {

    val repository = mock<GoogleMapsRepository>()

    @Test
    fun testSearchByQuery_getSearchResponse_singleCompleted() {
        whenever(repository.searchByQuery(anyString()))
                .thenReturn(Single.just(SearchResponseVO()))

        repository.searchByQuery(anyString())
                .test()
                .assertComplete()
                .isDisposed
    }

    @Test
    fun testSearchByQuery_getSearchResponse_singleError() {
        val response = Throwable("Error response")
        whenever(repository.searchByQuery(anyString()))
                .thenReturn(Single.error(response))

        repository.searchByQuery(anyString())
                .test()
                .assertError(response)
                .isTerminated
    }

}