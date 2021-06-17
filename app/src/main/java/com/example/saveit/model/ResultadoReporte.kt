package com.example.saveit.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultadoReporte(
    var Day: Long,
    val Value: Float
): Parcelable