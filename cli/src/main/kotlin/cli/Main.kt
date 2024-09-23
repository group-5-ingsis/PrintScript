package cli

import cli.CommandLineInterface.execute
import java.util.Scanner

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val scanner = Scanner(System.`in`)
        println("Welcome to the Command Line Interface. Type your commands below:")
        println("Format: | mainCommand | file | version | rules(if needed) |")

        while (true) {
            print("> ")
            val input = scanner.nextLine().trim()

            if (input.equals("exit", ignoreCase = true)) {
                println("Exiting...")
                break
            }

            val result = execute(input)
            println(result)
        }
    }
}
