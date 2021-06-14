package com.example.saveit.data

enum class MedioPago(val valor: Int, val descripcion: String) {
    TARJETA_DEBITO(1, "Tarjeta Debito"),
    TARJETA_CREDITO(2, "Tarjeta Credito"),
    EFECTIVO(3, "Efectivo"),
    QR(4, "QR");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }
    }
}