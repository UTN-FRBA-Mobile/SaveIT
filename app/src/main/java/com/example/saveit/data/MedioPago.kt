package com.example.saveit.data

enum class MedioPago(val valor: Int, val descripcion: String) {
    TARJETA_DEBITO(1, "Tarjeta de Débito"),
    TARJETA_CREDITO(2, "Tarjeta de Crédito"),
    EFECTIVO(3, "Efectivo"),
    QR(4, "QR");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }

        fun getByDescripcion(descripcion: String): MedioPago {
            return MedioPago.values().filter { it.descripcion == descripcion }.first()
        }
    }
}