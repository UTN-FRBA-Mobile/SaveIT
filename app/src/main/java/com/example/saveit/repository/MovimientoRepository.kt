package com.example.saveit.repository

import androidx.lifecycle.LiveData
import com.example.saveit.data.MovimientoDao
import com.example.saveit.model.Movimiento

class MovimientoRepository(private val movimientoDao: MovimientoDao) {
    val readAllData: LiveData<List<Movimiento>> = movimientoDao.readAllData()

    suspend fun addMovimiento(movimiento: Movimiento) {
        movimientoDao.addMovimiento(movimiento)
    }

    suspend fun updateMovimiento(movimiento: Movimiento) {
        movimientoDao.updateMovimiento(movimiento)
    }
}