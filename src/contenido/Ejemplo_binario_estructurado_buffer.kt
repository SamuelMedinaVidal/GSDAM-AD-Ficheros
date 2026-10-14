package contenido

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/binario.dat")
    Files.createDirectories(ruta.parent)

    DataOutputStream(
        BufferedOutputStream(
            Files.newOutputStream(ruta)
        )
    ).use { out ->
        out.writeInt(42)
        out.writeDouble(3.1416)
        out.writeUTF("K")
    }

    DataInputStream(
        BufferedInputStream(
            Files.newInputStream(ruta)
        )
    ).use { input ->
        val entero = input.readInt()
        val decimal = input.readDouble()
        val caracter = input.readUTF()

        println("📄 Contenido leído:")
        println("Int: $entero")
        println("Double: $decimal")
        println("Cadena: $caracter")
    }
}