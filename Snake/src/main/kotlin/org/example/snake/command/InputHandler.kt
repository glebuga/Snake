package org.example.snake.command

import javafx.scene.Scene
import javafx.scene.input.KeyCode

//Назначение: Обработка пользовательского ввода с клавиатуры.
//Действия:
//Содержит класс InputHandler, который принимает сцену и контроллер игры.
//При нажатии клавиши на сцене создается соответствующая команда DirectionCommand, которая затем выполняется.
class InputHandler(private val scene: Scene, private val gameController: GameController) {
    init {
        scene.setOnKeyPressed { event ->
            val direction = when (event.code) {
                KeyCode.UP -> Direction.UP
                KeyCode.DOWN -> Direction.DOWN
                KeyCode.LEFT -> Direction.LEFT
                KeyCode.RIGHT -> Direction.RIGHT
                else -> return@setOnKeyPressed
            }
            val command = DirectionCommand(direction, gameController)
            command.execute()
        }
    }
}