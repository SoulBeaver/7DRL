package com.sbg.arena.core.input

trait InputRequest {
    fun isValid(): Boolean
    fun execute()
}