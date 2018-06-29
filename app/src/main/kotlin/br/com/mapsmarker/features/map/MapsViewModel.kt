@file:Suppress("IMPLICIT_CAST_TO_ANY")

package br.com.mapsmarker.features.map

import android.arch.lifecycle.MutableLiveData
import br.com.mapsmarker.base.BaseViewModel
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

    private val locationData = MutableLiveData<LocationDTO>()

    fun getLocationData() = locationData

    fun getLocationStored(location: ResultVO) {
        disposables.add(createSingleCallable { useCase.searchLocation(location.placeId) }
                .subscribe( { locationData.value = it },
                            { locationData.value = null } )
        )
    }

    fun storeLocation(location: ResultVO?, shouldSave: Boolean) {
        location?.let {
            val locationDTO = LocationDTO(it, shouldSave)
            disposables.add(createSingleCallable {
                    if (shouldSave) useCase.insetLocation(locationDTO)
                    else useCase.deleteLocation(locationDTO)
                }.subscribe { _ -> locationData.value = locationDTO }
            )
        }
    }

    private inline fun <T> createSingleCallable(crossinline callable: () -> T): Single<T> {
        return Single.fromCallable { callable.invoke() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLatLngAverage(maxPosition: LatLngBO, minPosition: LatLngBO) = LatLng(
            (maxPosition.latitude + minPosition.latitude) / 2,
            (maxPosition.longitude + minPosition.longitude) / 2)

    fun getClosest(pos1: Double, pos2: Double, criterion: Double): Double {
        val diff1 = (pos1 - criterion).roundToPositive()
        val diff2 = (pos2 - criterion).roundToPositive()
        return if (diff1 < diff2) pos1 else pos2
    }

    private fun Double.roundToPositive() =
            if (this < 0) (this * -1)
            else this

}