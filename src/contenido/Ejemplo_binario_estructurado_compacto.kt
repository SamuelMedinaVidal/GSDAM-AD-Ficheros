package contenido

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

fun main() {
    val ruta = Path.of("documentos/registro.bin")

    FileChannel.open(
        ruta,
        StandardOpenOption.CREATE,
        StandardOpenOption.WRITE,
        StandardOpenOption.TRUNCATE_EXISTING
    ).use { canal ->
        val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + Double.SIZE_BYTES)
        buffer.putInt(42)
        buffer.putDouble(19.95)
        buffer.flip()
        canal.write(buffer)
    }

    FileChannel.open(ruta, StandardOpenOption.READ).use { canal ->
        val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + Double.SIZE_BYTES)
        canal.read(buffer)
        buffer.flip()
        val id = buffer.int
        val precio = buffer.double
        println("id=$id, precio=$precio")
    }
}