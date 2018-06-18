package br.com.mapsmarker.features.home

import br.com.mapsmarker.model.domain.SearchResponseVO
import br.com.mapsmarker.model.repository.GoogleMapsRepository
import io.reactivex.Single
import javax.inject.Inject

class HomeUseCase
@Inject constructor(private val repository: GoogleMapsRepository) {

    fun requestSearchByQuery(query: String): Single<SearchResponseVO> {
        return repository.searchByQuery(query)
    }

}