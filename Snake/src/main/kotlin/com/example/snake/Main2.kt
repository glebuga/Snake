package com.example.snake;

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.Duration
import java.awt.Point
import java.util.*

// Интерфейс для создания различных типов еды
interface FoodFactory {
    fun createFood(): Food
}

// Фабрика случайной еды
class RandomFoodFactory(private val foodImagePaths: Array<String>) : FoodFactory {
    private val random = Random()

    // Создание случайного типа еды
    override fun createFood(): Food {
        val imageUrl = this.javaClass.getResource(foodImagePaths[random.nextInt(foodImagePaths.size)])
        val image = Image(imageUrl.toExternalForm())
        return Food(image)
    }
}

// Класс, представляющий еду
class Food(val image: Image) : Cloneable {
    // Метод клонирования еды
    public override fun clone(): Food {
        return Food(this.image)
    }
}

// Интерфейс для наблюдателя
interface Observer {
    // Метод обновления наблюдателя
    fun update(score: Int)
}

// Интерфейс для подписчика
interface Subject {
    // Метод для добавления подписчика
    fun addObserver(observer: Observer)
    // Метод для удаления подписчика
    fun removeObserver(observer: Observer)
    // Метод для уведомления подписчиков
    fun notifyObservers()
}

// Класс игры, реализующий паттерн "наблюдатель"
class Game : Subject {
    private val observers = mutableListOf<Observer>()
    private var score = 0

    // Добавление наблюдателя
    override fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    // Удаление наблюдателя
    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    // Уведомление наблюдателей
    override fun notifyObservers() {
        observers.forEach { it.update(score) }
    }

    // Обновление счета
    fun updateScore(points: Int) {
        score += points
        notifyObservers()
    }
}

// Интерфейс для стратегии движения змейки
interface MoveStrategy {
    fun move(snakeHead: Point, snakeBody: MutableList<Point>, currentDirection: Int)
}

// Класс реализующий стратегию движения змейки по умолчанию
class DefaultMoveStrategy : MoveStrategy {
    override fun move(snakeHead: Point, snakeBody: MutableList<Point>, currentDirection: Int) {
        // Изменение координат головы змейки в зависимости от текущего направления
        when (currentDirection) {
            0 -> snakeHead.x++ // Вправо
            1 -> snakeHead.x-- // Влево
            2 -> snakeHead.y-- // Вверх
            3 -> snakeHead.y++ // Вниз
        }
    }
}

// Интерфейс для команды
interface Command {
    fun execute()
}

// Команда для движения змейки
class MoveCommand(
    private val snakeHead: Point,
    private val currentDirection: Int,
    private val snakeBody: MutableList<Point>,
    private val moveStrategy: MoveStrategy
) : Command {
    // Выполнение команды движения змейки
    override fun execute() {
        moveStrategy.move(snakeHead, snakeBody, currentDirection)
    }
}

// Основной класс приложения
class Main2 : Application(), Observer {
    private val WIDTH = 800.0
    private val HEIGHT = WIDTH
    private val ROWS = 20
    private val COLUMNS = ROWS
    private val SQUARE_SIZE = WIDTH / ROWS
    private val FOODS_IMAGE = arrayOf(
        "/img/ic_orange.png", "/img/ic_apple.png", "/img/ic_cherry.png",
        "/img/ic_berry.png", "/img/ic_coconut_.png", "/img/ic_peach.png", "/img/ic_watermelon.png", "/img/ic_orange.png",
        "/img/ic_pomegranate.png"
    )

    private val random = Random()
    private lateinit var gc: GraphicsContext
    private var snakeBody = mutableListOf<Point>()
    private lateinit var snakeHead: Point
    private var foodX = 0
    private var foodY = 0
    private var gameOver = false
    private var currentDirection = 0
    private var score = 0

    private lateinit var foodFactory: FoodFactory
    private lateinit var moveStrategy: MoveStrategy
    private lateinit var game: Game

    private val RIGHT = 0
    private val LEFT = 1
    private val UP = 2
    private val DOWN = 3

    // Метод запуска приложения
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Snake"
        val root = Group()
        val canvas = Canvas(WIDTH, HEIGHT)
        root.children.add(canvas)
        val scene = Scene(root)
        primaryStage.scene = scene
        primaryStage.show()
        gc = canvas.graphicsContext2D

        // Обработка нажатий клавиш
        scene.onKeyPressed = EventHandler { event: KeyEvent ->
            val code = event.code
            when (code) {
                KeyCode.RIGHT, KeyCode.D -> if (currentDirection != LEFT) {
                    val command = MoveCommand(snakeHead, currentDirection, snakeBody, moveStrategy)
                    command.execute()
                }
                KeyCode.LEFT, KeyCode.A -> if (currentDirection != RIGHT) {
                    val command = MoveCommand(snakeHead, currentDirection, snakeBody, moveStrategy)
                    command.execute()
                }
                KeyCode.UP, KeyCode.W -> if (currentDirection != DOWN) {
                    val command = MoveCommand(snakeHead, currentDirection, snakeBody, moveStrategy)
                    command.execute()
                }
                KeyCode.DOWN, KeyCode.S -> if (currentDirection != UP) {
                    val command = MoveCommand(snakeHead, currentDirection, snakeBody, moveStrategy)
                    command.execute()
                }
                else -> TODO()

            }
        }

        // Инициализация тела змейки
        for (i in 0 until 3) {
            snakeBody.add(Point(5, ROWS / 2))
        }
        snakeHead = snakeBody[0]

        // Инициализация фабрики еды
        foodFactory = RandomFoodFactory(FOODS_IMAGE)

        // Инициализация стратегии движения
        moveStrategy = DefaultMoveStrategy()

        // Инициализация игры и добавление этого класса как наблюдателя
        game = Game()
        game.addObserver(this)

        // Генерация еды
        generateFood()

        // Запуск игрового цикла
        val timeline = Timeline(KeyFrame(Duration.millis(130.0), EventHandler { event -> run() }))
        timeline.cycleCount = Animation.INDEFINITE
        timeline.play()
    }

    // Метод игрового цикла
    private fun run() {
        if (gameOver) {
            gc.fill = Color.RED
            gc.font = Font("Digital-7", 70.0)
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2)
            return
        }
        drawBackground()
        drawFood()
        drawSnake()
        drawScore()

        val command = MoveCommand(snakeHead, currentDirection, snakeBody, moveStrategy)
        command.execute()

        gameOver()
        eatFood()
    }

    // Отрисовка фона
    private fun drawBackground() {
        for (i in 0 until ROWS) {
            for (j in 0 until COLUMNS) {
                if ((i + j) % 2 == 0) {
                    gc.fill = Color.web("AAD751")
                } else {
                    gc.fill = Color.web("A2D149")
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE)
            }
        }
    }

    // Генерация случайной еды
    private fun generateFood() {
        val food = foodFactory.createFood()
        foodX = random.nextInt(ROWS)
        foodY = random.nextInt(COLUMNS)
        gc.drawImage(food.image, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE)
    }

    // Отрисовка змейки
    private fun drawSnake() {
        gc.fill = Color.web("4674E9")
        gc.fillRoundRect(snakeHead.x * SQUARE_SIZE, snakeHead.y * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35.0, 35.0)

        for (i in 1 until snakeBody.size) {
            gc.fillRoundRect(
                snakeBody[i].x * SQUARE_SIZE, snakeBody[i].y * SQUARE_SIZE, SQUARE_SIZE - 1,
                SQUARE_SIZE - 1, 20.0, 20.0
            )
        }
    }

    // Отрисовка еды
    private fun drawFood() {
        val food = foodFactory.createFood()
        gc.drawImage(food.image, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE)
    }

    // Проверка на конец игры
    private fun gameOver() {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH || snakeHead.y * SQUARE_SIZE >= HEIGHT) {
            gameOver = true
        }

        for (i in 1 until snakeBody.size) {
            if (snakeHead.x == snakeBody[i].x && snakeHead.y == snakeBody[i].y) {
                gameOver = true
                break
            }
        }
    }
    // Обработка съедания еды
    private fun eatFood() {
        if (snakeHead.x == foodX && snakeHead.y == foodY) {
            snakeBody.add(Point(-1, -1))
            score += 5
            game.updateScore(5)
            generateFood()
        }
    }

    // Отрисовка счета
    private fun drawScore() {
        gc.fill = Color.WHITE
        gc.font = Font("Digital-7", 35.0)
        gc.fillText("Score: $score", 10.0, 35.0)
    }

    // Метод обновления счета
    override fun update(score: Int) {
        this.score = score
    }
}

// Запуск приложения
fun main() {
    Application.launch(Main::class.java)
}
