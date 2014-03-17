package com.sbg.arena.core.enemy

data class Enemy(var hp: Int,
                 var attack: Int) {
    fun takeDamage(damage: Int) {
        hp -= damage
    }

    fun isDead() = (hp <= 0)
}