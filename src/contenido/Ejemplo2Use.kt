package contenido

import java.io.FileWriter

fun main() {
    FileWriter("archivo.txt").use {
        it.write("Hola, Kotlin\n")
    }
}