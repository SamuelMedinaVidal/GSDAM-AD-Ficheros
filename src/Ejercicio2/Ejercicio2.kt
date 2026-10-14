package Ejercicio2

import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.Scanner
import kotlin.streams.toList

fun main() {
    val scanner = Scanner(System.`in`)
    val homePath = Paths.get(System.getProperty("user.home"))

    var salir = false
    while (!salir) {
        mostrarInformacionHome(homePath)
        mostrarMenu()

        when (scanner.nextLine()) {
            "1" -> crearDirectorioEnHome(homePath, scanner)
            "2" -> eliminarDirectorioDeHome(homePath, scanner)
            "3" -> verContenidoRecursivo(homePath, scanner)
            "4" -> crearArchivoDeTexto(homePath, scanner)
            "5" -> leerArchivoDeTexto(homePath, scanner)
            "6" -> encriptarOcultarArchivo(homePath, scanner)
            "7" -> copiarUnArchivo(homePath, scanner)
            "0" -> salir = true
            else -> println("Opción no válida. Introduce un número correcto.")
        }
    }
}

fun mostrarInformacionHome(homePath: Path) {
    try {
        val elementos = Files.list(homePath)
            .filter { !it.fileName.toString().startsWith(".") }
            .limit(5)
            .toList()

        elementos.forEach { path ->
            val atributos = Files.readAttributes(path, BasicFileAttributes::class.java)

            val tipo = if (atributos.isDirectory) "Directorio" else if (atributos.isRegularFile) "Archivo" else "Otro"
            val tamano = if (atributos.isRegularFile) "${atributos.size()} bytes" else "0 bytes"

            println("Nombre: ${path.fileName}")
            println("Tipo: $tipo")
            println("Tamaño: $tamano")
            println("Creado: ${atributos.creationTime()}")
            println("Modificado: ${atributos.lastModifiedTime()}")
            println("Legible: ${Files.isReadable(path)}")
            println("Escribible: ${Files.isWritable(path)}\n")
        }

        println("Información del sistema de archivos: ")
        val fileStore = Files.getFileStore(homePath)
        println("-Tipo: ${fileStore.type()}")
        println("-Total: ${fileStore.totalSpace} bytes")
        println("-Libre: ${fileStore.usableSpace} bytes\n")
    } catch (e: Exception) {
        println("Error al acceder a la información del sistema: ${e.message}")
    }
}

fun mostrarMenu() {
    println("1. Crear un directorio en el home")
    println("2. Eliminar un directorio del home")
    println("3. Ver contenido de un directorio del home")
    println("4. Crear un archivo de texto")
    println("5. Leer un archivo de texto")
    println("6. Encriptar/Ocultar archivo (Texto a Binario)")
    println("7. Copiar un archivo a otra ruta")
    println("0. Salir")
    print("Elige una opción: ")
}

fun crearDirectorioEnHome(homePath: Path, scanner: Scanner) {
    print("Introduce el nombre del nuevo directorio: ")
    val nombreDirectorio = scanner.nextLine()
    val nuevoPath = homePath.resolve(nombreDirectorio)

    try {
        Files.createDirectory(nuevoPath)
        println("Directorio '${nombreDirectorio}' creado.")
    } catch (e: FileAlreadyExistsException) {
        println("Error: El directorio '${nombreDirectorio}' ya existe")
    } catch (e: IOException) {
        println("Error de entrada/salida: ${e.message}")
    }
}

fun eliminarDirectorioDeHome(homePath: Path, scanner: Scanner) {
    print("Introduce el nombre del directorio a eliminar: ")
    val nombreDirectorio = scanner.nextLine()
    val pathAEliminar = homePath.resolve(nombreDirectorio)

    try {
        Files.delete(pathAEliminar)
        println("Directorio '${nombreDirectorio}' eliminado.")
    } catch (e: NoSuchFileException) {
        println("Error: El directorio '${nombreDirectorio}' no existe.")
    } catch (e: DirectoryNotEmptyException) {
        println("Error: El directorio no está vacío. No se puede eliminar.")
    } catch (e: IOException) {
        println("Error al intentar eliminar: ${e.message}")
    }
}

fun verContenidoRecursivo(homePath: Path, scanner: Scanner) {
    print("Introduce el nombre del directorio a explorar (pulsa Enter para el home): ")
    val nombreDirectorio = scanner.nextLine()

    val targetPath = if (nombreDirectorio.isNotBlank()) homePath.resolve(nombreDirectorio) else homePath

    if (!Files.exists(targetPath) || !Files.isDirectory(targetPath)) {
        println("Error: La ruta no existe o no es un directorio.")
        return
    }

    println("\nExplorando de forma recursiva: $targetPath")
    try {
        Files.walk(targetPath).use { stream ->
            stream.forEach { path ->
                println(" - $path")
            }
        }
    } catch (e: AccessDeniedException) {
        println("Error de permisos: No tienes acceso a ciertos subdirectorios.")
    } catch (e: IOException) {
        println("Error de entrada/salida: ${e.message}")
    }
}

fun crearArchivoDeTexto(homePath: Path, scanner: Scanner) {
    println("Introduce el nombre del archivo a crear: ")
    val nombreArchivo = scanner.nextLine()
    val pathArchivo = homePath.resolve(nombreArchivo)

    println("Escribe las líneas de texto que quieras (escribe 'FIN' en una línea nueva para terminar): ")
    val lineas = mutableListOf<String>()
    while (true){
        val linea = scanner.nextLine()
        if (linea == "FIN") break
        lineas.add(linea)
    }

    try {
        Files.write(pathArchivo, lineas, StandardCharsets.UTF_8)
        println("Archivo ${nombreArchivo} creado correctamente.")
    } catch (e: IOException) {
        println("Error al guardar el archivo: ${e.message}")
    }
}

fun leerArchivoDeTexto(homePath: Path, scanner: Scanner) {
    println("Introduce el nombre del archivo de texto a leer: ")
    val nombreArchivo = scanner.nextLine()
    val pathArchivo = homePath.resolve(nombreArchivo)

    try {
        Files.newBufferedReader(pathArchivo, StandardCharsets.UTF_8).use { reader ->
            println("\nContenido de ${nombreArchivo}")
            reader.lineSequence().forEach { println(it)}
        }
    } catch (e: NoSuchFileException){
        println("Error: El archivo: ${nombreArchivo} no existe. Comprueba el nombre y vuélvelo a intentar.")
    } catch (e: IOException) {
        println("Error de lectura: ${e.message}")
    }

}

fun encriptarOcultarArchivo(homePath: Path, scanner: Scanner) {
    println("Introduce el nombre del archivo de texto a ocultar/encriptar: ")
    val nombreArchivo = scanner.nextLine()
    val pathOrigen  = homePath.resolve(nombreArchivo)

    val nombreSinExtension = nombreArchivo.substringBeforeLast('.')
    val pathDestino = homePath.resolve("${nombreSinExtension}.bin")

    try {
        val bytesOriginales = Files.readAllBytes(pathOrigen)

        //Creamos un nuevo array iterando sobre cada byte y sumándole 1
        val bytesEncriptados = ByteArray(bytesOriginales.size) { i ->
            (bytesOriginales[i] + 1).toByte()
        }
        Files.write(pathDestino, bytesEncriptados)
        println("Archivo encriptado y guardado con éxito como ${pathDestino.fileName}.")
    } catch (e: NoSuchFileException) {
        println("Error: El archivo origen ${nombreArchivo} no existe.")
    } catch (e: IOException) {
        println("Error al encriptar el archivo ${e.message}")
    }
}

fun copiarUnArchivo(homePath: Path, scanner: Scanner) {
    println("Introduce el nombre del archivo de texto: ")
    val nombreOrigen = scanner.nextLine()
    val pathOrigen = homePath.resolve(nombreOrigen)

    println("Introduce el nombre de la copia (destino): ")
    val nombreDestino = scanner.nextLine()
    val pathDestino = homePath.resolve(nombreDestino)

    try {
        Files.copy(pathOrigen, pathDestino, StandardCopyOption.REPLACE_EXISTING)
        println("Archivo ${nombreOrigen} copiado a ${nombreDestino} exitosamente.")
    } catch (e: NoSuchFileException) {
        println("Error: El archivo original no existe en el directorio.")
    } catch (e: IOException) {
        println("Error al intentar copiar el archivo: ${e.message}")
    }

}