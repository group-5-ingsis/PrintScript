package application

import cli.CommandLineInterface
import cli.FileReader
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File

object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    Application.launch(CLIApplication::class.java, *args)
  }
}

class CLIApplication : Application() {
  private val versions = listOf("1.0", "1.1")
  private val recentCommands = mutableListOf<String>()

  override fun start(primaryStage: Stage) {
    // Define UI elements
    val inputField = TextField()
    inputField.promptText = "Enter command"

    val outputArea = TextArea()
    outputArea.isEditable = false

    val executeButton = Button("Execute")

    // Labels
    val commandLabel = Label("Select Operation:")
    val versionLabel = Label("Select Version:")
    val fileLabel = Label("Available Files:")
    val recentCommandsLabel = Label("Recent Commands:")

    // ComboBoxes
    val commandComboBox = ComboBox<String>()
    commandComboBox.items.addAll(listOf("validate", "execute", "format", "analyze"))
    commandComboBox.promptText = "Select operation"

    val versionComboBox = ComboBox<String>()
    versionComboBox.items.addAll(versions)
    versionComboBox.promptText = "Select version"

    // ListViews
    val availableFilesListView = ListView<String>()
    val recentCommandsListView = ListView<String>()

    // Initial setup
    val initialVersion = versions.first()
    val availableFiles = getAvailableFiles(initialVersion)
    availableFilesListView.items.addAll(availableFiles)
    recentCommandsListView.items.addAll(recentCommands)

    // Actions
    executeButton.setOnAction {
      val commandText = inputField.text
      val result = CommandLineInterface.execute(commandText)
      outputArea.appendText("Command: $commandText\nResult: $result\n\n")
      inputField.clear()
      updateRecentCommands(commandText)
      recentCommandsListView.items.clear()
      recentCommandsListView.items.addAll(recentCommands)
    }

    commandComboBox.setOnAction {
      val selectedCommand = commandComboBox.value
      val selectedFile = availableFilesListView.selectionModel.selectedItem
      val selectedVersion = versionComboBox.value
      if (selectedCommand != null && selectedFile != null && selectedVersion != null) {
        inputField.text = "$selectedCommand $selectedFile $selectedVersion"
      }
    }

    versionComboBox.setOnAction {
      val selectedVersion = versionComboBox.value
      if (selectedVersion != null) {
        val availableFiles = getAvailableFiles(selectedVersion)
        availableFilesListView.items.clear()
        availableFilesListView.items.addAll(availableFiles)
      }
    }

    availableFilesListView.setOnMouseClicked { event ->
      if (event.clickCount == 2) {
        val selectedFile = availableFilesListView.selectionModel.selectedItem
        val selectedCommand = commandComboBox.value
        val selectedVersion = versionComboBox.value
        if (selectedFile != null && selectedCommand != null && selectedVersion != null) {
          inputField.text = "$selectedCommand $selectedFile $selectedVersion"
        }
      }
    }

    recentCommandsListView.setOnMouseClicked { event ->
      if (event.clickCount == 2) {
        val selectedCommand = recentCommandsListView.selectionModel.selectedItem
        if (selectedCommand != null) {
          inputField.text = selectedCommand
        }
      }
    }

    // Layout
    val layout =
      VBox(
        10.0,
        commandLabel, commandComboBox,
        versionLabel, versionComboBox,
        fileLabel, availableFilesListView,
        inputField, executeButton, outputArea,
        recentCommandsLabel, recentCommandsListView,
      )
    val scene = Scene(layout, 600.0, 500.0) // Increased height to accommodate all elements

    primaryStage.title = "Command Line Interface"
    primaryStage.scene = scene
    primaryStage.show()
  }

  private fun updateRecentCommands(command: String) {
    if (command.isNotEmpty() && !recentCommands.contains(command)) {
      recentCommands.add(0, command)
      if (recentCommands.size > 10) {
        recentCommands.removeAt(recentCommands.size - 1)
      }
    }
  }

  private fun getAvailableFiles(version: String): List<String> {
    val resourceDirectory = "ps/$version"
    val fileNames = mutableListOf<String>()

    val resource = FileReader::class.java.classLoader.getResource(resourceDirectory)
    if (resource != null) {
      val resourceURI = resource.toURI()
      if (resourceURI.scheme == "file") {
        val directory = File(resourceURI)
        if (directory.isDirectory) {
          fileNames.addAll(directory.listFiles()?.map { it.name } ?: emptyList())
        }
      } else {
        // Handle case for resources inside JAR (if needed)
        throw UnsupportedOperationException("Listing files inside JARs is not supported in this example.")
      }
    }

    return fileNames
  }
}
