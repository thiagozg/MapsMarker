package br.com.mapsmarker.model.api

import br.com.mapsmarker.model.api.StatusEnum.ERROR
import br.com.mapsmarker.model.api.StatusEnum.SUCCESS

class StateResponse<T>
constructor(val status: StatusEnum, val data: T? = null,
            val error: Throwable? = null, val isLoading: Boolean = false) {
    companion object {

        fun <T> success(data: T): StateResponse<T> {
            return StateResponse(SUCCESS, data)
        }

        fun <T> error(error: Throwable): StateResponse<T> {
            return StateResponse(ERROR, error = error)
        }

    }
}
