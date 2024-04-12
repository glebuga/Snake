package org.example.snake.controller

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

//Назначение: Отображение игрового состояния на графическом интерфейсе пользователя (UI).
//Действия:
//Содержит класс UIController, который принимает экземпляры Canvas и GameModel.
//Метод drawGame() рисует змейку, еду и отображает счет на канвасе.
class UIController(
    private val canvas: Canvas,
    private val gameModel: GameModel
) {
    private val cellSize = 20.0

    fun drawGame() {
        val graphicsContext = canvas.graphicsContext2D
        graphicsContext.clearRect(0.0, 0.0, canvas.width, canvas.height)

        drawSnake(graphicsContext)
        drawFood(graphicsContext)
        drawScore(graphicsContext)
    }

    private fun drawSnake(graphicsContext: GraphicsContext) {
        graphicsContext.fill = Color.GREEN
        gameModel.getSnakeBody().forEach { (row, col) ->
            graphicsContext.fillRect(col * cellSize, row * cellSize, cellSize, cellSize)
        }
    }

    private fun drawFood(graphicsContext: GraphicsContext) {
        val foodPosition = gameModel.getFoodPosition()
        graphicsContext.fill = Color.RED
        graphicsContext.fillRect(foodPosition.second * cellSize, foodPosition.first * cellSize, cellSize, cellSize)
    }

    private fun drawScore(graphicsContext: GraphicsContext) {
        graphicsContext.fill = Color.BLACK
        graphicsContext.fillText("Score: ${gameModel.getScore()}", 10.0, 20.0)
    }
}