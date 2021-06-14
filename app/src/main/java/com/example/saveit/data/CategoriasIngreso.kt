package com.example.saveit.data

enum class CategoriasIngreso(val valor: Int, val descripcion: String) {
    PLAZOFIJO(1, "Plazo Fijo"),
    PRESTAMO(2, "Prestamo"),
    SUELDO(3, "Sueldo"),
    VENTAS(4, "Ventas"),
    OTRO(5, "Otro");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }
    }
}