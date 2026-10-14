package contenido

import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO

fun main() {
    val originalPath = Path.of("documentos/penyagolosa.png")
    val copiaPath = Path.of("documentos/penyagolosa_copia.png")
    val modificadaPath = Path.of("documentos/penyagolosa_modificada.png")

    if (!Files.exists(originalPath)) {
        println("No se encuentra la imagen original: $originalPath")
        return
    }

    Files.copy(originalPath, copiaPath, StandardCopyOption.REPLACE_EXISTING)
    println("Imagen copiada a: $copiaPath")

    val imagen: BufferedImage = ImageIO.read(copiaPath.toFile())

    for (x in 0 until imagen.width) {
        for (y in 0 until imagen.height) {
            val color = Color(imagen.getRGB(x, y))
            val invertido = Color(255 - color.red, 255 - color.green, 255 - color.blue)
            imagen.setRGB(x, y, invertido.rgb)
        }
    }

    ImageIO.write(imagen, "png", modificadaPath.toFile())
    println("Imagen modificada guardada como: $modificadaPath")
}