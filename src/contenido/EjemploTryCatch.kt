package contenido

fun main() {
    try {
        val resultado = 10 / 0
        println("Resultado: $resultado")
    } catch (e: ArithmeticException) {
        println("Error: no se puede dividir entre cero.")
    } finally {
        println("Fin del bloque try-catch.")
    }
}