package br.com.mapsmarker.features.home

import br.com.mapsmarker.model.domain.SearchResponseVO
import br.com.mapsmarker.model.repository.GoogleMapsRepository
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

class HomeUseCase
@Inject constructor(private val repository: GoogleMapsRepository) {

    @Inject protected lateinit var gson: Gson

    fun requestSearchByQuery(query: String): Single<SearchResponseVO> {
        return repository.searchByQuery(query)
    }

}