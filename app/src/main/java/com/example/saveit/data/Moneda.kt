package com.example.saveit.data

enum class Moneda(val valor: Int, val descripcion: String) {
    PESO(1, "$"),
    DOLAR(2, "u\$s");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }
    }
}