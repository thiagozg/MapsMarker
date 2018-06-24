package br.com.mapsmarker.model.api

sealed class StateResponse<T>(val data: T? = null,
                              val error: Throwable? = null)

class StateSuccess<T>(data: T) : StateResponse<T>(data = data)
class StateError(error: Throwable) : StateResponse<Throwable>(error = error)
