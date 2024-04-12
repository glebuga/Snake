import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage) {
        val root = StackPane()
        val scene = Scene(root, 800.0, 600.0)
        primaryStage.scene = scene
        primaryStage.title = "Snake Game"
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}