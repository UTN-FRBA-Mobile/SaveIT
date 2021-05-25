package com.example.saveit.data

import androidx.lifecycle.LiveData

class MovimientoRepository(private val movimientoDao: MovimientoDao) {
    val readAllData: LiveData<List<Movimiento>> = movimientoDao.readAllData()

    suspend fun addMovimiento(movimiento: Movimiento){
        movimientoDao.addMovimiento(movimiento)
    }
}