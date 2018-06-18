package br.com.mapsmarker.base

import br.com.mapsmarker.model.domain.ResultVO

interface UiListeners {

    interface OnClickListener {
        fun onClick(placesList: List<ResultVO>)
    }

}