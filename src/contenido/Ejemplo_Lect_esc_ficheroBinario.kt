package contenido

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/datos.bin")

    val datos = byteArrayOf(1, 2, 3, 4, 5)
    Files.write(ruta, datos)
    println("Archivo binario creado: ${ruta.toAbsolutePath()}")

    val bytes = Files.readAllBytes(ruta)
    println("Contenido leido (byte a byte):")
    for (b in bytes) {
        print("$b ")
    }
}