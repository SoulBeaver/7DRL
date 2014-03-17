package com.sbg.arena.core.input

trait InputRequest {
    fun initialize()
    fun execute()
    fun isValid(): Boolean
}

fun initialize(initializer: () -> InputRequest): () -> InputRequest {
    return {
        val request = initializer()
        request.initialize()
        request
    }
}