package com.example.saveit.data

enum class MedioPago(val valor: Int, val descripcion: String) {
    TARJETA_DEBITO(1, "Tarjeta Credito"),
    TARJETA_CREDITO(2, "Tarjeta Debito"),
    EFECTIVO(3, "Efectivo"),
    QR(4, "QR")
}