package com.example.saveit.data

enum class CategoriasGasto(val valor: Int, val descripcion: String)  {
    AHORRO(1, "Ahorro"),
    ALQUILE(2, "Alquiler"),
    CASA(3, "Casa"),
    COMIDA(4, "Comida"),
    DELIVERY(5, "Delivery"),
    EDUCACION(6, "Educacion"),
    ENTRETENIMIENTO(7, "Entretenimiento"),
    GASOLINA(8, "Gasolina"),
    MASCOTA(9, "Mascota"),
    MERCADERIA(10, "Mercadera"),
    PERSONALES(11, "Personales"),
    RESTAURANTE(12, "Restaurante"),
    ROPA(13, "Ropa"),
    SALUD(14, "Salud"),
    SERVICIOS(15, "Servicios"),
    TRANSPORTE(16, "Transporte"),
    OTROS(17, "Otros");

    companion object {
        fun getByValor(valor: Int): String {
            return values().filter { it.valor == valor }.first().descripcion
        }
    }
}