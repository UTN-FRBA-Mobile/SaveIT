package com.example.saveit.data
enum class Meses(val valor: Int, val descripcion: String) {
    ENERO(0, "Enero"),
    FEBRERO(1, "Febrero"),
    MARZO(2, "Marzo"),
    ABRIL(3, "Abril"),
    MAYO(4, "Mayo"),
    JUNIO(5, "Junio"),
    JULIO(6, "Julio"),
    AGOSTO(7, "Agosto"),
    SEPTIEMBRE(8, "Septiembre"),
    OCTUBRE(9, "Octubre"),
    NOVIEMBRE(10, "Noviembre"),
    DICIEMBRE(11, "Diciembre");

    companion object {
        fun getByDescripcion(descripcion: String): Int {
            return values().filter { it.descripcion == descripcion }.first().valor
        }
    }
}