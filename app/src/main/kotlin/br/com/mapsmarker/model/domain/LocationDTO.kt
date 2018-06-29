package br.com.mapsmarker.model.domain

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
class LocationDTO(
        @PrimaryKey
        var placeId: String,
        var formattedAddress: String,
        var latitude: Double,
        var longitude: Double) {

    @Ignore
    var isSaved: Boolean = true

    @Ignore
    constructor(resultVO: ResultVO, isSaveAction: Boolean) :
            this(resultVO.placeId,
                    resultVO.formattedAddress,
                    resultVO.geometry.location.lat,
                    resultVO.geometry.location.lng) {
        this.isSaved = isSaveAction
    }

}