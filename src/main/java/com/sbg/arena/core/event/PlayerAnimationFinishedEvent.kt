package com.sbg.arena.core.event

import com.sbg.arena.core.input.InputRequest

data class PlayerAnimationStartedEvent(val request: InputRequest)
data class PlayerAnimationFinishedEvent(val request: InputRequest)
data class AllAnimationsFinishedEvent