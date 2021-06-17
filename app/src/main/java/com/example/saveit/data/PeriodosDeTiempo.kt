package com.example.saveit.data

enum class PeriodosDeTiempo(val valor: Int, val descripcion: String, val query_str: String) {
    SEMANA(1, "Semana", "-7 days"),
    MES(2, "Mes", "-30 days"),
    TRES_MESES(3, "Tres Meses", "-90 days"),
    SEIS_MESES(4, "Seis Meses", "-180 days"),
    ANIO(4, "Año", "-365 days");

    companion object {
        fun getByDescripcion(descripcion: String): PeriodosDeTiempo {
            return values().filter { it.descripcion == descripcion }.first()
        }
    }
}
