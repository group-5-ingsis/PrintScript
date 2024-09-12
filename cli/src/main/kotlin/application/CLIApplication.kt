package application

import cli.CommandLineInterface
import cli.FileReader
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
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

    // ProgressBar
    private val progressBar = ProgressBar(0.0) // Initially at 0% progress

    override fun start(primaryStage: Stage) {
        // Define UI elements
        val inputField = TextField()
        inputField.promptText = "Enter command"

        val outputArea = TextArea()
        outputArea.isEditable = false
        outputArea.prefHeight = 300.0

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
        val yamlFilesListView = ListView<String>()
        recentCommandsListView.prefHeight = 100.0
        availableFilesListView.prefHeight = 200.0
        yamlFilesListView.prefHeight = 200.0

        // Initial setup
        val initialVersion = versions.first()
        val availableFiles = getAvailableFiles(initialVersion)
        availableFilesListView.items.addAll(availableFiles)
        recentCommandsListView.items.addAll(recentCommands)
        yamlFilesListView.items.addAll(getYamlFiles(initialVersion))

        // ProgressBar settings
        progressBar.prefWidth = 400.0 // Set the width of the progress bar
        progressBar.progress = 0.0 // Start with 0 progress

        // Actions
        executeButton.setOnAction {
            val commandText = inputField.text

            // Start the progress bar
            progressBar.progress = 0.1 // Initial progress indicator
            outputArea.appendText("Executing command...\n")

            // Simulate progress update (you can tie this to actual command execution)
            val result = CommandLineInterface.execute(commandText)
            outputArea.appendText("Command: $commandText\nResult: $result\n\n")

            // Update the progress bar when the task is done
            progressBar.progress = 1.0 // Complete

            inputField.clear()
            updateRecentCommands(commandText)
            recentCommandsListView.items.clear()
            recentCommandsListView.items.addAll(recentCommands)
        }

        // Layout using BorderPane and HBox for side-by-side ListViews
        val filesHBox = HBox(10.0, availableFilesListView, yamlFilesListView)
        val leftSide = VBox(10.0, commandLabel, commandComboBox, versionLabel, versionComboBox, fileLabel, filesHBox)
        val rightSide = VBox(10.0, recentCommandsLabel, recentCommandsListView)
        val bottomSide = VBox(10.0, inputField, executeButton, progressBar, outputArea) // Add the progressBar to the layout

        val layout = BorderPane()
        layout.left = leftSide
        layout.right = rightSide
        layout.bottom = bottomSide

        val scene = Scene(layout, 1000.0, 600.0)
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
                    fileNames.addAll(directory.listFiles { _, name -> name.endsWith(".ps") }?.map { it.name } ?: emptyList())
                }
            } else {
                throw UnsupportedOperationException("Listing files inside JARs is not supported in this example.")
            }
        }
        return fileNames
    }

    private fun getYamlFiles(version: String): List<String> {
        val resourceDirectory = "ps/$version"
        val yamlFiles = mutableListOf<String>()
        val resource = FileReader::class.java.classLoader.getResource(resourceDirectory)
        if (resource != null) {
            val resourceURI = resource.toURI()
            if (resourceURI.scheme == "file") {
                val directory = File(resourceURI)
                if (directory.isDirectory) {
                    yamlFiles.addAll(directory.listFiles { _, name -> name.endsWith(".yaml") }?.map { it.name } ?: emptyList())
                }
            } else {
                throw UnsupportedOperationException("Listing files inside JARs is not supported in this example.")
            }
        }
        return yamlFiles
    }
}
