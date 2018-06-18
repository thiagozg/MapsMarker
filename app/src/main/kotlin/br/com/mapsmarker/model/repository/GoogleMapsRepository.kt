package br.com.mapsmarker.model.repository

import br.com.mapsmarker.model.api.GoogleMapsApi
import br.com.mapsmarker.model.domain.SearchResponseVO
import io.reactivex.Single
import javax.inject.Inject

class GoogleMapsRepository
@Inject constructor() {

    @Inject lateinit var api: GoogleMapsApi

    fun searchByQuery(query: String): Single<SearchResponseVO> {
        return api.searchByQuery(query)
    }

}