package com.example.saveit.data

enum class Categoria(val valor: Int, val descripcion: String) {
    AHORRO(1, "Ahorro"),
    ALIMENTOS(2, "Alimento"),
    TRANSPORTE(3, "Transporte"),
    IMPUESTOS(4, "Impuestos"),
    OCIO(5, "Ocio");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }

        fun getByDescripcion(descripcion: String): Categoria {
            return Categoria.values().filter { it.descripcion == descripcion }.first()
        }
    }
}