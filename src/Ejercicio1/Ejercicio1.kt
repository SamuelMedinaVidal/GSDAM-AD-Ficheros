package Ejercicio1

import java.io.IOException
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