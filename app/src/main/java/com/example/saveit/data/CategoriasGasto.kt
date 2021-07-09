package com.example.saveit.data

enum class CategoriasGasto(val valor: Int, val descripcion: String)  {
    AHORRO(1, "Ahorro"),
    ALQUILE(2, "Alquiler"),
    CASA(3, "Casa"),
    COMIDA(4, "Comida"),
    DELIVERY(5, "Delivery"),
    EDUCACION(6, "Educacion"),
    GASOLINA(7, "Gasolina"),
    PERSONALES(8, "Personales"),
    SERVICIOS(9, "Servicios"),
    OTROS(10, "Otros");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }

        fun getByDescripcion(descripcion: String): CategoriasGasto {
            return CategoriasGasto.values().filter { it.descripcion == descripcion }.first()
        }
    }
}