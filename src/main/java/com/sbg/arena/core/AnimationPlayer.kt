package com.sbg.arena.core

import com.sbg.arena.core.animation.Animation
import java.util.ArrayList
import org.newdawn.slick.Graphics
import kotlin.properties.Delegates
import com.sbg.arena.core.level.Skin

class AnimationPlayer(val levelSkin: Skin) {
    private var activeAnimations: MutableList<Pair<Animation, () -> Unit>> = ArrayList<Pair<Animation, () -> Unit>>()
    private var onAllAnimationsFinished: () -> Unit by Delegates.notNull()

    fun update() {
        activeAnimations.filter { it.first.isFinished() }
                        .forEach { it.first.finish(); it.second() }

        activeAnimations = activeAnimations.filter { !it.first.isFinished() } as MutableList

        if (activeAnimations.isNotEmpty())
            activeAnimations.forEach { it.first.update() }
        else
            onAllAnimationsFinished()
    }

    fun render(graphics: Graphics) {
        activeAnimations.forEach { it.first.render(graphics!!) }
    }

    fun play(animationWithFinisher: Pair<Animation, () -> Unit>) {
        val (animation, _) = animationWithFinisher
        animation.initialize(levelSkin)
        activeAnimations.add(animationWithFinisher)
    }

    fun isPlaying() = activeAnimations.isNotEmpty()

    fun onAllAnimationsFinished(action: () -> Unit) {
        this.onAllAnimationsFinished = action
    }
}