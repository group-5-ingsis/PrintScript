package cli

import builder.CommandBuilder
import builder.ValidationCommandBuilder
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage

class CommandLineInterface(private val commandBuilders: MutableMap<String, CommandBuilder>) {
  private fun initializeCommandBuilders() {
    commandBuilders["validate"] = ValidationCommandBuilder()
  }

  // Formato de los commands:
  // validate helloWorld.ps 1.0
  // formatting helloWorld.ps 1.0 formatRules.yml
  fun execute(command: String): String {
    initializeCommandBuilders()

    val file = CommandParser.getFile(command)
    val operation = CommandParser.getOperation(command)
    val version = CommandParser.getVersion(command)
    val arguments = CommandParser.getArguments(command)

    val builder = commandBuilders[operation] ?: return "Unknown command: $command"

    val cmd = builder.build(file, arguments, version)

    return cmd.execute()
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      Application.launch(CLIApplication::class.java, *args)
    }
  }
}

class CLIApplication : Application() {
  override fun start(primaryStage: Stage) {
    val commandLineInterface = CommandLineInterface(mutableMapOf())

    val inputField = TextField()
    val outputArea = TextArea()
    outputArea.isEditable = false

    val executeButton = Button("Execute")
    executeButton.setOnAction {
      val command = inputField.text
      val result = commandLineInterface.execute(command)
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
