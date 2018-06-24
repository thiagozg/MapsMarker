package br.com.mapsmarker.features.home

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import br.com.mapsmarker.model.api.StateResponse
import br.com.mapsmarker.model.domain.SearchResponseVO
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

class HomeViewModelTest {
    
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var schedulersRule = RxSchedulerRule()

    val useCase = mock<HomeUseCase>()
    val observerState = mock<Observer<StateResponse<SearchResponseVO>>>()

    val viewModel by lazy { HomeViewModel(useCase) }

    @Before
    fun setUp() {
        reset(useCase, observerState)
    }

    @Test
    fun testSearchByQuery_getResponse_successful() {
        whenever(useCase.requestSearchByQuery(anyString()))
                .thenReturn(Single.just(SearchResponseVO()))

        val query = "Springfield"

        viewModel.getResponse().observeForever(observerState)
        viewModel.searchByQuery(query)

        verify(useCase).requestSearchByQuery(query)

        val argumentCaptor = ArgumentCaptor.forClass(StateResponse::class.java)
        argumentCaptor.run {
            verify(observerState, times(1))
                    .onChanged(capture() as StateResponse<SearchResponseVO>?)
            assertFalse(viewModel.loadingStatus.value!!)
        }
    }

    @Ignore
    @Test
    fun testSearchByQuery_getResponse_error() {
        val response = Throwable("Error response")
        whenever(useCase.requestSearchByQuery(anyString()))
                .thenReturn(Single.error(response))

        viewModel.getResponse().observeForever(observerState)
        viewModel.searchByQuery(anyString())

        verify(useCase).requestSearchByQuery(anyString())

        val argumentCaptor = ArgumentCaptor.forClass(Throwable::class.java)
        val expectedErrorState = StateResponse(StatusEnum.ERROR, null, error = Throwable("Error."))
        argumentCaptor.run {
//            verify(observerState, times(1))
//                    .onChanged(capture())
            val (errorState) = allValues
            assertEquals(errorState, expectedErrorState)
            assertFalse(viewModel.loadingStatus.value!!)
        }
    }

}