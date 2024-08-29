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

    override fun start(primaryStage: Stage) {
        // Define UI elements
        val inputField = TextField()
        inputField.promptText = "Enter command"

        val outputArea = TextArea()
        outputArea.isEditable = false
        outputArea.prefHeight = 300.0 // Increased height for the output area

        val executeButton = Button("Execute")

        // Labels
        val commandLabel = Label("Select Operation:")
        val versionLabel = Label("Select Version:")
        val fileLabel = Label("Available Files:")
        val recentCommandsLabel = Label("Recent Commands:")
        val yamlFilesLabel = Label("YAML Files:")

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
        recentCommandsListView.prefHeight = 100.0 // Reduced height for the recent commands list

        // Increased height for available files and YAML files list views
        availableFilesListView.prefHeight = 200.0
        yamlFilesListView.prefHeight = 200.0

        // Initial setup
        val initialVersion = versions.first()
        val availableFiles = getAvailableFiles(initialVersion)
        availableFilesListView.items.addAll(availableFiles)
        recentCommandsListView.items.addAll(recentCommands)
        yamlFilesListView.items.addAll(getYamlFiles(initialVersion))

        // Actions
        executeButton.setOnAction {
            val commandText = inputField.text
            val result = CommandLineInterface.execute(commandText)
            outputArea.appendText("Command: $commandText\nResult: $result\n\n")
            outputArea.requestLayout() // Ensures the layout updates
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

                // Also update YAML files list
                yamlFilesListView.items.clear()
                yamlFilesListView.items.addAll(getYamlFiles(selectedVersion))
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

        yamlFilesListView.setOnMouseClicked { event ->
            if (event.clickCount == 2) {
                val selectedYamlFile = yamlFilesListView.selectionModel.selectedItem
                if (selectedYamlFile != null) {
                    inputField.text = "${inputField.text} $selectedYamlFile"
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

        // Layout using BorderPane and HBox for side-by-side ListViews
        val filesHBox = HBox(10.0, availableFilesListView, yamlFilesListView)
        val leftSide = VBox(10.0, commandLabel, commandComboBox, versionLabel, versionComboBox, fileLabel, filesHBox)
        val rightSide = VBox(10.0, recentCommandsLabel, recentCommandsListView)
        val bottomSide = VBox(10.0, inputField, executeButton, outputArea)

        val layout = BorderPane()
        layout.left = leftSide
        layout.right = rightSide
        layout.bottom = bottomSide

        val scene = Scene(layout, 1000.0, 600.0) // Increased size for better layout management

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
                // Handle case for resources inside JAR (if needed)
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
                // Handle case for resources inside JAR (if needed)
                throw UnsupportedOperationException("Listing files inside JARs is not supported in this example.")
            }
        }

        return yamlFiles
    }
}
