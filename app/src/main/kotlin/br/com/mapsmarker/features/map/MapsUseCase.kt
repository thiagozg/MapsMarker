package br.com.mapsmarker.features.map

import br.com.mapsmarker.model.data.LocationDao
import br.com.mapsmarker.model.domain.LocationDTO
import javax.inject.Inject

class MapsUseCase
@Inject constructor(private val locationDao: LocationDao) {

    fun searchLocation(placeId: String) = locationDao.getLocationById(placeId)

    fun insetLocation(location: LocationDTO) = locationDao.insertLocation(location)

    fun deleteLocation(location: LocationDTO) = locationDao.deleteLocation(location)

}