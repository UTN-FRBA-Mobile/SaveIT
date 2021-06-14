package com.example.saveit.data

enum class Categoria(val valor: Int, val descripcion: String) {
    ALIMENTOS(1, "Alimento"),
    TRANSPORTE(2, "Transporte"),
    IMPUESTOS(3, "Impuestos"),
    OCIO(4, "Ocio");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }
    }
}