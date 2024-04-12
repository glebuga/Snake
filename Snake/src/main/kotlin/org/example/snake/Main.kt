package org.example.snake

import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage) {
        val scoreLabel = Label("Score: 0") // Создаем метку для отображения счета
        scoreLabel.style = "-fx-font-size: 20px;" // Устанавливаем размер шрифта

        val root = StackPane(scoreLabel).apply {
            alignment = Pos.TOP_LEFT // Выравниваем метку в левом верхнем углу
        }

        val scene = Scene(root, 800.0, 800.0)
        primaryStage.scene = scene
        primaryStage.title = "Snake Game"
        primaryStage.show()
    }
}

fun main() {
    Application.launch(Main::class.java)
}