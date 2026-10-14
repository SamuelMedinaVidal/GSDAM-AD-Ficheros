package contenido

import java.io.File

fun main() {
    val archivo = File("datos.txt")

    try {
        archivo.bufferedReader().use { reader ->
            println("Primera línea: ${reader.readLine()}")
        }
    } catch (e: Exception) {
        println("Error al leer el archivo: ${e.message}")
    }
}