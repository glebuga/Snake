package org.example.snake.command

//Назначение: Представление конкретной команды изменения направления движения змейки.
//Действия:
//Содержит класс DirectionCommand, который принимает направление и контроллер игры.
//Метод execute() вызывает метод контроллера игры для обработки изменения направления змейки.
class DirectionCommand(private val direction: Direction, private val gameController: GameController) : Command {
    override fun execute() {
        gameController.handleInput(direction)
    }
}