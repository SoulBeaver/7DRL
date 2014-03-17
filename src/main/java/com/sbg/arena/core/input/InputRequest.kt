package com.sbg.arena.core.input

trait InputRequest {
    fun initialize()
    fun execute()
    fun isValid(): Boolean
}

fun initialize(request: InputRequest): () -> InputRequest {
    request.initialize()

    return { request }
}