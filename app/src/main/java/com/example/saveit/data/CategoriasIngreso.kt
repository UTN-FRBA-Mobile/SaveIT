package com.example.saveit.data

enum class CategoriasIngreso(val valor: Int, val descripcion: String) {
    AHORRO(1, "Ahorro"),
    PLAZOFIJO(2, "Plazo Fijo"),
    PRESTAMO(3, "Prestamo"),
    SUELDO(4, "Sueldo"),
    VENTAS(5, "Ventas"),
    OTRO(6, "Otro");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }

        fun getByDescripcion(descripcion: String): CategoriasIngreso {
            return values().first { it.descripcion == descripcion }
        }
    }
}