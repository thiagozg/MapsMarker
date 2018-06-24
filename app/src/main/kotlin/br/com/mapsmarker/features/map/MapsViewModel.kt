@file:Suppress("IMPLICIT_CAST_TO_ANY")

package br.com.mapsmarker.features.map

import android.arch.lifecycle.MutableLiveData
import br.com.mapsmarker.base.BaseViewModel
import br.com.mapsmarker.model.api.StateError
import br.com.mapsmarker.model.api.StateResponse
import br.com.mapsmarker.model.api.StateSuccess
import br.com.mapsmarker.model.domain.LatLngBO
import br.com.mapsmarker.model.domain.LocationDTO
import br.com.mapsmarker.model.domain.ResultVO
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapsViewModel
@Inject constructor(private val useCase: MapsUseCase) : BaseViewModel() {

    private val viewResponse = MutableLiveData<StateResponse<*>>()

    fun getLocationStored(location: ResultVO) = useCase.searchLocation(location.placeId)

    fun storeLocation(location: ResultVO?, shouldSave: Boolean) {
        location?.let {
            disposables.add(Single.fromCallable {
                if (shouldSave)
                    useCase.insetLocation(LocationDTO(it))
                else
                    useCase.deleteLocation(LocationDTO(it))
                }.getSingleStoreLocation()
            )
        }
    }

    private fun Single<*>.getSingleStoreLocation() =
            subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( { viewResponse.value = StateSuccess(it) },
                                { viewResponse.value = StateError(it) })

    fun getLatLngAverage(maxPosition: LatLngBO, minPosition: LatLngBO) = LatLng(
            (maxPosition.latitude + minPosition.latitude) / 2,
            (maxPosition.longitude + minPosition.longitude) / 2)

    fun getClosestToMax(pos1: Double, pos2: Double, criterion: Double) =
            if (criterion - pos1 < criterion - pos2) pos1
            else pos2

    fun getClosestToMin(pos1: Double, pos2: Double, criterion: Double) =
            if (criterion - pos1 < criterion - pos2) pos2
            else pos1

}