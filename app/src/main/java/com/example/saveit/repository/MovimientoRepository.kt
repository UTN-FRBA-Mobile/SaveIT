package com.example.saveit.repository

import androidx.lifecycle.LiveData
import com.example.saveit.data.MovimientoDao
import com.example.saveit.model.Movimiento
import com.example.saveit.model.ResultadoReporte

class MovimientoRepository(private val movimientoDao: MovimientoDao) {
    val readAllData: LiveData<List<Movimiento>> = movimientoDao.readAllData()

    fun readAllDataBetween(desde: Long, hasta: Long): LiveData<List<Movimiento>> = movimientoDao.readAllDataBetween(desde, hasta)

    val readAhorros: LiveData<List<Movimiento>> = movimientoDao.readAhorros()

    fun readAhorrosBetween(desde: Long, hasta: Long): LiveData<List<Movimiento>> = movimientoDao.readAhorrosBetween(desde, hasta)

    fun readIngresos(desde: Long, hasta: Long): LiveData<Double> = movimientoDao.readIngresos(desde, hasta)

    fun readGastos(desde: Long, hasta: Long): LiveData<Double> = movimientoDao.readGastos(desde, hasta)

    fun readSpecificTimeDataCategoryPayment(moneda: Int, categoria: Int, medioPago: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> = movimientoDao.readSpecificTimeDataCategoryPayment(moneda, categoria, medioPago, tipoMovimiento, desde)

    fun readSpecificTimeDataCategoryAll(moneda: Int, medioPago: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> = movimientoDao.readSpecificTimeDataCategoryAll(moneda, medioPago, tipoMovimiento, desde)

    fun readSpecificTimeDataPaymentAll(moneda: Int, categoria: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> = movimientoDao.readSpecificTimeDataPaymentAll(moneda, categoria, tipoMovimiento, desde)

    fun readSpecificTimeData(moneda: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> = movimientoDao.readSpecificTimeData(moneda, tipoMovimiento, desde)

    suspend fun addMovimiento(movimiento: Movimiento) {
        movimientoDao.addMovimiento(movimiento)
    }

    suspend fun updateMovimiento(movimiento: Movimiento) {
        movimientoDao.updateMovimiento(movimiento)
    }
}