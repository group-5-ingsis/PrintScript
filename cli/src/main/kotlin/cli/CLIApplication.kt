package cli

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage

object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    Application.launch(CLIApplication::class.java, *args)
  }
}

class CLIApplication : Application() {
  override fun start(primaryStage: Stage) {
    val inputField = TextField()
    val outputArea = TextArea()
    outputArea.isEditable = false

    val executeButton = Button("Execute")
    executeButton.setOnAction {
      val command = inputField.text
      val result = CommandLineInterface.execute(command)
      outputArea.appendText("Command: $command\nResult: $result\n\n")
      inputField.clear()
    }

    val layout = VBox(10.0, inputField, executeButton, outputArea)
    val scene = Scene(layout, 600.0, 400.0)

    primaryStage.title = "CLI Interface"
    primaryStage.scene = scene
    primaryStage.show()
  }
}
