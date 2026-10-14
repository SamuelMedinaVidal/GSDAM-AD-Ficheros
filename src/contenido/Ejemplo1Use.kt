package contenido

import java.io.BufferedReader
import java.io.FileReader

fun main() {
    BufferedReader(FileReader("archivo.txt")).use { reader ->
        val linea = reader.readLine()
        println(linea)
    }
}