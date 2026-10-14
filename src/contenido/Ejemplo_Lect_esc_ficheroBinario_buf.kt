package contenido

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/datos.bin")
    val datos = byteArrayOf(1, 2, 3, 4, 5)

    BufferedOutputStream(Files.newOutputStream(ruta)).use { salida ->
        salida.write(datos)
    }

    println("Archivo binario creado: ${ruta.toAbsolutePath()}")

    BufferedInputStream(Files.newInputStream(ruta)).use { entrada ->
        println("Contenido leído (byte a byte):")

        var byte = entrada.read()
        while (byte != -1) {
            print("$byte ")
            byte = entrada.read()
        }
    }
}