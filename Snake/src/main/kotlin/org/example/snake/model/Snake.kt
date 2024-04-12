package org.example.snake.model

//Назначение: Представление модели змейки в игре.
//Действия:
//Содержит класс Snake, который управляет поведением змейки, включая ее движение, рост и взаимодействие с едой.
class Snake(initialPosition: List<Pair<Int, Int>>, private val gameModel: GameModel) {
    private val body: MutableList<Pair<Int, Int>> = mutableListOf()

    init {
        body.addAll(initialPosition)
    }

    fun move(direction: Direction) {
        val head = body.first()
        val (nextRow, nextCol) = when (direction) {
            Direction.UP -> Pair(head.first - 1, head.second)
            Direction.DOWN -> Pair(head.first + 1, head.second)
            Direction.LEFT -> Pair(head.first, head.second - 1)
            Direction.RIGHT -> Pair(head.first, head.second + 1)
        }

        // Проверка на столкновение со стеной или собственным хвостом
        if (nextRow < 0 || nextRow >= gameModel.rows || nextCol < 0 || nextCol >= gameModel.cols || body.contains(Pair(nextRow, nextCol))) {
            gameModel.endGame()
            return
        }

        // Добавление новой головы змейки
        body.add(0, Pair(nextRow, nextCol))

        // Проверка на наличие еды
        if (gameModel.isFood(nextRow, nextCol)) {
            gameModel.increaseScore()
            gameModel.removeFood(nextRow, nextCol)
            gameModel.generateFood()
        } else {
            // Если нет еды, удалить хвост
            body.removeAt(body.size - 1)
        }
    }

    fun getBody(): List<Pair<Int, Int>> {
        return body.toList()
    }
}