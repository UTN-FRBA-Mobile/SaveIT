package com.example.saveit.data

enum class TipoMovimiento (val valor:Int) {
    INGRESO(0),
    EGRESO(1);

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().toString()
        }
    }
}