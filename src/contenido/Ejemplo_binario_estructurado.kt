package contenido

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/binario.dat")
    Files.createDirectories(ruta.parent)

    DataOutputStream(Files.newOutputStream(ruta)).use { out ->
        out.writeInt(42)
        out.writeDouble(3.1416)
        out.writeUTF("K")
    }

    val fis = FileInputStream(ruta.toFile())
    val input = DataInputStream(fis)
    val entero = input.readInt()
    val decimal = input.readDouble()
    val caracter = input.readUTF()
    input.close()
    fis.close()

    println("📄 Contenido leído:")
    println("  Int: $entero")
    println("  Double: $decimal")
    println("  Char: $caracter")
}