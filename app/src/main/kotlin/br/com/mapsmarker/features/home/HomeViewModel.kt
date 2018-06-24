package br.com.mapsmarker.features.home

import android.arch.lifecycle.MutableLiveData
import br.com.mapsmarker.base.BaseViewModel
import br.com.mapsmarker.model.api.StateError
import br.com.mapsmarker.model.api.StateResponse
import br.com.mapsmarker.model.api.StateSuccess
import br.com.mapsmarker.model.domain.SearchResponseVO
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel
@Inject constructor(private val useCase: HomeUseCase) : BaseViewModel() {

    private val viewResponse = MutableLiveData<StateResponse<*>>()

    fun searchByQuery(query: String) {
        loadResultList(useCase.requestSearchByQuery(query))
    }

    fun getResponse(): MutableLiveData<StateResponse<*>> {
        return viewResponse
    }

    private fun loadResultList(single: Single<SearchResponseVO>) {
        disposables.add(single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingStatus.setValue(true) }
                .doAfterTerminate { loadingStatus.setValue(false) }
                .subscribe( { viewResponse.value = StateSuccess(it) },
                            { viewResponse.value = StateError(it) })
        )
    }

}