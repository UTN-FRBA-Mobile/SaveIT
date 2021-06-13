package com.example.saveit.repository

import androidx.lifecycle.LiveData
import com.example.saveit.data.MovimientoDao
import com.example.saveit.model.Movimiento

class MovimientoRepository(private val movimientoDao: MovimientoDao) {
    val readAllData: LiveData<List<Movimiento>> = movimientoDao.readAllData()

    fun readIngresos(desde: Long, hasta: Long): LiveData<Double> = movimientoDao.readIngresos(desde, hasta)

    fun readGastos(desde: Long, hasta: Long): LiveData<Double> = movimientoDao.readGastos(desde, hasta)

    suspend fun addMovimiento(movimiento: Movimiento) {
        movimientoDao.addMovimiento(movimiento)
    }

    suspend fun updateMovimiento(movimiento: Movimiento) {
        movimientoDao.updateMovimiento(movimiento)
    }
}