package contenido

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.*

fun main() {
    val path = Paths.get("documentos/datos.txt")

    FileChannel.open(path, READ, WRITE, CREATE).use { canal ->
        canal.position(0)
        val inicio = ByteBuffer.wrap("Inicio\n".toByteArray())
        canal.write(inicio)

        canal.position(20)
        val texto = ByteBuffer.wrap("Texto en posición 20\n".toByteArray())
        canal.write(texto)

        val bufferLectura = ByteBuffer.allocate(1024)
        canal.position(0)
        canal.read(bufferLectura)

        bufferLectura.flip()
        val contenido = String(bufferLectura.array(), 0, bufferLectura.limit())
        println("Contenido leído del archivo:\n$contenido")
    }
}