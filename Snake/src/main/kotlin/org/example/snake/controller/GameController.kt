package org.example.snake.controller
//Назначение: Обработка пользовательского ввода и передача соответствующих команд модели игры.
//Действия:
//Содержит класс GameController, который принимает экземпляр GameModel.
//Метод handleInput() обрабатывает ввод пользователя и вызывает соответствующие методы модели игры для обновления состояния игры.
class GameController(private val gameModel: GameModel) {
    fun handleInput(direction: Direction) {
        gameModel.moveSnake(direction)
    }
}