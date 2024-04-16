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

class Main : Application() {

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

    private val RIGHT = 0
    private val LEFT = 1
    private val UP = 2
    private val DOWN = 3

    private lateinit var gc: GraphicsContext
    private var snakeBody = mutableListOf<Point>()
    private lateinit var snakeHead: Point
    private lateinit var foodImage: Image
    private var foodX = 0
    private var foodY = 0
    private var gameOver = false
    private var currentDirection = RIGHT
    private var score = 0

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Snake"
        val root = Group()
        val canvas = Canvas(WIDTH, HEIGHT)
        root.children.add(canvas)
        val scene = Scene(root)
        primaryStage.scene = scene
        primaryStage.show()
        gc = canvas.graphicsContext2D

        scene.onKeyPressed = EventHandler { event: KeyEvent ->
            val code = event.code
            when (code) {
                KeyCode.RIGHT, KeyCode.D -> if (currentDirection != LEFT) {
                    currentDirection = RIGHT
                }
                KeyCode.LEFT, KeyCode.A -> if (currentDirection != RIGHT) {
                    currentDirection = LEFT
                }
                KeyCode.UP, KeyCode.W -> if (currentDirection != DOWN) {
                    currentDirection = UP
                }
                KeyCode.DOWN, KeyCode.S -> if (currentDirection != UP) {
                    currentDirection = DOWN
                }
            }
        }

        for (i in 0 until 3) {
            snakeBody.add(Point(5, ROWS / 2))
        }
        snakeHead = snakeBody[0]
        generateFood()

        val timeline = Timeline(KeyFrame(Duration.millis(130.0)) { run() })
        timeline.cycleCount = Animation.INDEFINITE
        timeline.play()
    }

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

        for (i in snakeBody.size - 1 downTo 1) {
            snakeBody[i].x = snakeBody[i - 1].x
            snakeBody[i].y = snakeBody[i - 1].y
        }

        when (currentDirection) {
            RIGHT -> moveRight()
            LEFT -> moveLeft()
            UP -> moveUp()
            DOWN -> moveDown()
        }

        gameOver()
        eatFood()
    }

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

    private fun generateFood() {
        while (true) {
            foodX = (Math.random() * ROWS).toInt()
            foodY = (Math.random() * COLUMNS).toInt()

            for (snake in snakeBody) {
                if (snake.x.toDouble() == foodX.toDouble() && snake.y.toDouble() == foodY.toDouble()) {
                    continue
                }
            }
            foodImage = Image(FOODS_IMAGE[(Math.random() * FOODS_IMAGE.size).toInt()])
            break
        }
    }

    private fun drawFood() {
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE)
    }

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

    private fun moveRight() {
        snakeHead.x++
    }

    private fun moveLeft() {
        snakeHead.x--
    }

    private fun moveUp() {
        snakeHead.y--
    }

    private fun moveDown() {
        snakeHead.y++
    }

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

    private fun eatFood() {
        if (snakeHead.x == foodX && snakeHead.y == foodY) {
            snakeBody.add(Point(-1, -1))
            generateFood()
            score += 5
        }
    }

    private fun drawScore() {
        gc.fill = Color.WHITE
        gc.font = Font("Digital-7", 35.0)
        gc.fillText("Score: $score", 10.0, 35.0)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(*args)
        }
    }
}