package org.example.snake.command

//Назначение: Определение интерфейса для команды.
//Действия:
//Содержит интерфейс Command, который определяет метод execute(), используемый для выполнения команды.
interface Command {
    fun execute()
}