package org.example.snake.model

import org.example.snake.observer.Observer
import kotlin.random.Random

//Назначение: Модель игры, которая управляет состоянием игры, змейкой, едой и счетом игрока.
//Действия:
//Содержит класс GameModel, который хранит данные о состоянии игры и предоставляет методы для изменения этого состояния.
class GameModel(private val rows: Int, private val cols: Int) {
    private var isGameOver: Boolean = false
    private var score: Int = 0
    private val snake: Snake
    private val observers: MutableList<Observer> = mutableListOf()
    private var foodPosition: Pair<Int, Int> = generateFoodPosition()

    init {
        val initialSnakePosition = listOf(Pair(rows / 2, cols / 2))
        snake = Snake(initialSnakePosition, this)
    }

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    fun notifyObservers() {
        observers.forEach { it.update() }
    }

    fun moveSnake(direction: Direction) {
        if (!isGameOver) {
            snake.move(direction)
            notifyObservers()
        }
    }

    fun endGame() {
        isGameOver = true
        notifyObservers()
    }

    fun increaseScore() {
        score++
        notifyObservers()
    }

    fun isFood(row: Int, col: Int): Boolean {
        return foodPosition == Pair(row, col)
    }

    fun removeFood(row: Int, col: Int) {
        foodPosition = generateFoodPosition()
    }

    fun generateFood() {
        foodPosition = generateFoodPosition()
    }

    private fun generateFoodPosition(): Pair<Int, Int> {
        var row: Int
        var col: Int
        do {
            row = Random.nextInt(rows)
            col = Random.nextInt(cols)
        } while (snake.getBody().contains(Pair(row, col)))
        return Pair(row, col)
    }

    fun getSnakeBody(): List<Pair<Int, Int>> {
        return snake.getBody()
    }

    fun getFoodPosition(): Pair<Int, Int> {
        return foodPosition
    }

    fun getScore(): Int {
        return score
    }

    fun isGameOver(): Boolean {
        return isGameOver
    }
}