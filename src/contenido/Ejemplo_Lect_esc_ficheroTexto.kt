package contenido

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/texto.txt")

    // Escritura en fichero de texto
    val lineasParaGuardar = listOf(
        "Primera linea",
        "Segunda linea",
        "Hola desde Kotlin"
    )
    Files.write(ruta, lineasParaGuardar, StandardCharsets.UTF_8)
    println("Fichero de texto escrito.")

    // 1) readAllLines: devuelve lista de lineas
    val lineasLeidas = Files.readAllLines(ruta, StandardCharsets.UTF_8)
    println("Contenido leido con readAllLines:")
    for (linea in lineasLeidas) {
        println(linea)
    }

    // 2) readString: devuelve todo el texto en un String
    val contenido = Files.readString(ruta, StandardCharsets.UTF_8)
    println("Contenido leido con readString:")
    println(contenido)

    // 3) newBufferedReader: lectura secuencial (util en archivos grandes)
    Files.newBufferedReader(ruta, StandardCharsets.UTF_8).use { reader ->
        println("Contenido leido con newBufferedReader:")
        reader.lineSequence().forEach { println(it) }
    }
}