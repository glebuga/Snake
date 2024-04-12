package org.example.snake.model
class Food(private val position: Pair<Int, Int>) {
    fun getPosition(): Pair<Int, Int> {
        return position
    }
}