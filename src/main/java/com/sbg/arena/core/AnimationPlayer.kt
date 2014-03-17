package com.sbg.arena.core

import com.sbg.arena.core.animation.Animation
import java.util.ArrayList
import org.newdawn.slick.Graphics
import kotlin.properties.Delegates
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.input.InputRequest
import com.sbg.arena.core.input.MoveRequest
import com.sbg.arena.core.animation.MoveAnimation
import com.sbg.arena.core.input.ToggleWallRequest
import com.sbg.arena.core.animation.ToggleWallAnimation
import com.sbg.arena.core.input.ShootRequest
import com.sbg.arena.core.animation.ShootAnimation

class AnimationPlayer(val levelSkin: Skin) {
    private var activeAnimations: MutableList<Animation> = ArrayList<Animation>()
    private var onAllAnimationsFinished: () -> Unit by Delegates.notNull()

    fun update() {
        activeAnimations.filter { it.isFinished() }
                        .forEach { it.finish() }

        activeAnimations = activeAnimations.filter { !it.isFinished() } as MutableList

        if (activeAnimations.isNotEmpty())
            activeAnimations.forEach { it.update() }
        else
            onAllAnimationsFinished()
    }

    fun render(graphics: Graphics) {
        activeAnimations.forEach { it.render(graphics) }
    }

    fun play(animation: Animation) {
        animation.initialize(levelSkin)
        activeAnimations.add(animation)
    }

    fun isPlaying() = activeAnimations.isNotEmpty()

    fun onAllAnimationsFinished(action: () -> Unit) {
        this.onAllAnimationsFinished = action
    }
}

fun toAnimation(request: InputRequest, onAnimationFinished: () -> Unit = { request.execute() }): Animation {
    return when (request) {
        is MoveRequest       -> MoveAnimation(request, onAnimationFinished)
        is ToggleWallRequest -> ToggleWallAnimation(request, onAnimationFinished)
        is ShootRequest      -> ShootAnimation(request, onAnimationFinished)
        else                 -> throw IllegalArgumentException("Unrecognized request:  $request")
    }
}