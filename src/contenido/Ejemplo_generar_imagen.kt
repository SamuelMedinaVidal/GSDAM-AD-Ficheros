package contenido

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val ancho = 200
    val alto = 100
    val imagen = BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB)

    for (x in 0 until ancho) {
        for (y in 0 until alto) {
            val rojo = (x * 255) / ancho
            val verde = (y * 255) / alto
            val azul = 128
            val color = Color(rojo, verde, azul)
            imagen.setRGB(x, y, color.rgb)
        }
    }

    val archivo = File("documentos/imagen_generada.png")
    ImageIO.write(imagen, "png", archivo)
    println("✅ Imagen generada correctamente: ${archivo.absolutePath}")
}