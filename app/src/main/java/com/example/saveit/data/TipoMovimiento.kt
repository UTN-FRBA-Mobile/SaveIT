package com.example.saveit.data

enum class TipoMovimiento (val valor:Int, val descripcion:String) {
    INGRESO(0, "Ingreso"),
    EGRESO(1, "Egreso");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().toString()
        }
    }
}