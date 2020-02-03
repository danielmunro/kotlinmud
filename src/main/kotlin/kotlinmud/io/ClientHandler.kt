package kotlinmud.io

import kotlinmud.Calculator
import kotlinmud.action.Command
import java.net.Socket
import java.nio.charset.Charset
import java.util.Scanner
import java.io.OutputStream

class ClientHandler(private val client: Socket) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private val calculator: Calculator = Calculator()
    private var running: Boolean = false
    val buffer: MutableList<String> = arrayListOf()

    fun run() {
        running = true
        write("Welcome to the server!\n" +
                "To Exit, write: 'EXIT'.\n" +
                "To use the calculator, input two numbers separated with a space and an operation in the ending\n" +
                "Example: 5 33 multi\n" +
                "Available operations: 'add', 'sub', 'div', 'multi'")

        while (running) {
            try {
                val text = reader.nextLine().toLowerCase()
                if (text == Command.EXIT.toString()) {
                    shutdown()
                    continue
                }
                val values = text.split(' ')
                val result = calculator.calculate(values[0].toInt(), values[1].toInt(), values[2])
                write(result)
                buffer.add(text)
            } catch (ex: Exception) {
                shutdown()
            }

        }
    }

    fun isRunning(): Boolean {
        return running
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

}
