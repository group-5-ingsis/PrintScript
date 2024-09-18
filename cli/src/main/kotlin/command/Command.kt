package command

interface Command {
    fun execute(): String
    fun getProgress(): Int
}
