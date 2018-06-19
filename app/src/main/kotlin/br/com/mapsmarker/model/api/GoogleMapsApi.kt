package br.com.mapsmarker.model.api

import br.com.mapsmarker.BuildConfig.API_KEY
import br.com.mapsmarker.features.Constants.COMMON_PARAM
import br.com.mapsmarker.model.domain.SearchResponseVO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {

    @GET(COMMON_PARAM + API_KEY)
    fun searchByQuery(@Query("address") query: String): Single<SearchResponseVO>

}