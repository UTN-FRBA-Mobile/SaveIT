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

    fun readUltimaCotizacion(): LiveData<Double> = movimientoDao.readUltimaCotizacion()

    fun readSpecificTimeDataCategoryPayment(categoria: Int, medioPago: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> = movimientoDao.readSpecificTimeDataCategoryPayment(categoria, medioPago, tipoMovimiento, desde)

    fun readSpecificTimeDataCategoryAll(medioPago: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> = movimientoDao.readSpecificTimeDataCategoryAll(medioPago, tipoMovimiento, desde)

    fun readSpecificTimeDataPaymentAll(categoria: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> = movimientoDao.readSpecificTimeDataPaymentAll(categoria, tipoMovimiento, desde)

    fun readSpecificTimeData(tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> = movimientoDao.readSpecificTimeData(tipoMovimiento, desde)

    suspend fun addMovimiento(movimiento: Movimiento) {
        movimientoDao.addMovimiento(movimiento)
    }

    suspend fun updateMovimiento(movimiento: Movimiento) {
        movimientoDao.updateMovimiento(movimiento)
    }
}