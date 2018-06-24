package br.com.mapsmarker.features.home

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import br.com.mapsmarker.model.api.StateError
import br.com.mapsmarker.model.api.StateResponse
import br.com.mapsmarker.model.api.StateSuccess
import br.com.mapsmarker.model.domain.SearchResponseVO
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*



class HomeViewModelTest {
    
    @get:Rule
    val archRule = InstantTaskExecutorRule()

    @get:Rule
    var schedulersRule = RxSchedulerRule()

    val useCase = mock<HomeUseCase>()
    val observerState = mock<Observer<StateResponse<*>>>()
    val observerLoading = mock<Observer<Boolean>>()
    val viewModel by lazy { spy(HomeViewModel(useCase)) }

    @After
    fun tearDown() {
        reset(useCase, observerState)
    }

    @Test
    fun testSearchByQuery_shouldViewResponseHasStateSuccess() {
        // Arrange
        val searchResponseVO = SearchResponseVO()
        whenever(useCase.requestSearchByQuery(anyString()))
                .thenReturn(Single.just(searchResponseVO))

        // Act
        viewModel.getResponse().observeForever(observerState)
        viewModel.loadingStatus.observeForever(observerLoading)
        viewModel.searchByQuery(anyString())
        viewModel.getResponse().removeObserver(observerState)
        viewModel.loadingStatus.removeObserver(observerLoading)

        // Assert
        val argumentCaptor = ArgumentCaptor.forClass(StateResponse::class.java)
        val expectedSuccesState = StateSuccess(searchResponseVO)
        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (successState) = allValues
            assertThat(successState.data, instanceOf(SearchResponseVO::class.java))
            assertEquals(expectedSuccesState.data, successState.data)
        }
        assertFalse(viewModel.loadingStatus.value!!)
    }

    @Test
    fun testSearchByQuery_shouldViewResponseHasStateError() {
        // Arrange
        val errorResponse = Throwable("Error response")
        whenever(useCase.requestSearchByQuery(anyString()))
                .thenReturn(Single.error(errorResponse))

        // Act
        viewModel.getResponse().observeForever(observerState)
        viewModel.loadingStatus.observeForever(observerLoading)
        viewModel.searchByQuery(anyString())
        viewModel.getResponse().removeObserver(observerState)
        viewModel.loadingStatus.removeObserver(observerLoading)

        // Assert
        val argumentCaptor = ArgumentCaptor.forClass(StateResponse::class.java)
        val expectedErrorState = StateError(error = errorResponse)
        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (errorState) = allValues
            assertThat(errorState.error, instanceOf(Throwable::class.java))
            assertEquals(errorState.error, expectedErrorState.error)
        }
        assertFalse(viewModel.loadingStatus.value!!)
    }

    @Test
    fun testSearchByQuery_shouldHasLoadingStatus() {
        // Arrange
        val searchResponseVO = mock<SearchResponseVO>()
        val delayer = PublishSubject.create<Boolean>()

        whenever(useCase.requestSearchByQuery(anyString()))
                .thenReturn(Single.just<SearchResponseVO>(searchResponseVO)
                        .delaySubscription(delayer))

        // Act
        viewModel.loadingStatus.observeForever(observerLoading)
        viewModel.searchByQuery(anyString())
        viewModel.loadingStatus.removeObserver(observerLoading)

        // Assert
        assertTrue(viewModel.loadingStatus.value!!)
        delayer.onComplete()
        assertFalse(viewModel.loadingStatus.value!!)
    }

}