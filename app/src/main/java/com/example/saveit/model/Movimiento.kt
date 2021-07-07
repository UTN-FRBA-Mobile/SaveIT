package com.example.saveit.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movimiento_table")
data class Movimiento(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val monto: Double,
    val moneda: Int,
    val medioDePago: Int, // Referencia al enum de medios de pago
    val categoria: Int,
    val fecha: Long,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val tipoMovimiento: Int,
    val cotizacionDolar: Double
): Parcelable