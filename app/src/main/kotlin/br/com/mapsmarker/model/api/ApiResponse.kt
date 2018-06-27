package br.com.mapsmarker.model.api

import br.com.mapsmarker.model.api.StatusEnum.ERROR
import br.com.mapsmarker.model.api.StatusEnum.SUCCESS

/**
 * ApiResponse holder provided to the UI
 *
 * @param <T>
 */
class ApiResponse<T>
constructor(val status: StatusEnum, val data: T? = null,
            val error: Throwable? = null, val isLoading: Boolean = false) {
    companion object {

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(SUCCESS, data)
        }

        fun <T> error(error: Throwable): ApiResponse<T> {
            return ApiResponse(ERROR, error = error)
        }

    }
}
