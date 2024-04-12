package org.example.snake.observer

import org.example.snake.model.GameModel

class ScoreObserver(private val gameModel: GameModel) : Observer {
    override fun update() {
        val score = gameModel.score
        // Здесь можно добавить логику обновления пользовательского интерфейса для отображения текущего счета
        println("Score updated: $score")
    }

}