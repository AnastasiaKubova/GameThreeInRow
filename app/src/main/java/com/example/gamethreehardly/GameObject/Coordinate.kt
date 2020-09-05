package com.example.gamethreehardly.GameObject

class Coordinate(val x: Float, val y: Float) {

    override fun hashCode(): Int {
        return (x.toString() + y.toString()).toInt()
    }

    fun equals(coords: Coordinate?): Boolean {
        return coords != null && x == coords.x && y == coords.y
    }
}