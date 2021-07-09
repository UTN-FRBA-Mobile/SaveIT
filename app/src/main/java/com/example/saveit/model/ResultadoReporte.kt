package com.example.saveit.model

import android.os.Parcelable
import com.example.saveit.data.Moneda
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultadoReporte(
    var Day: Long,
    val Value: Float,
    val Moneda: Int
): Parcelable