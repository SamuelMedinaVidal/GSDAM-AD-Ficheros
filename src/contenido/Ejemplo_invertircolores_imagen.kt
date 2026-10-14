package contenido

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val archivoEntrada = File("documentos/imagen_generada.png")
    val archivoSalida = File("documentos/imagen_salida.png")

    val imagen: BufferedImage = ImageIO.read(archivoEntrada)

    for (x in 0 until imagen.width) {
        for (y in 0 until imagen.height) {
            val colorOriginal = Color(imagen.getRGB(x, y))
            val colorInvertido = Color(
                255 - colorOriginal.red,
                255 - colorOriginal.green,
                255 - colorOriginal.blue
            )
            imagen.setRGB(x, y, colorInvertido.rgb)
        }
    }

    ImageIO.write(imagen, "png", archivoSalida)
    println("✅ Imagen guardada como ${archivoSalida.name}")
}
