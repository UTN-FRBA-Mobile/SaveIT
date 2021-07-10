package com.example.saveit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.saveit.data.MovimientoDatabase
import com.example.saveit.model.Movimiento
import com.example.saveit.model.ResultadoReporte
import com.example.saveit.repository.MovimientoRepository
import com.example.saveit.retrofit.DolarService
import com.example.saveit.retrofit.IDolarService

class ReporteFechaViewModel(application: Application): AndroidViewModel(application) {
    private val readAllData: LiveData<List<Movimiento>>
    private val repository: MovimientoRepository

    init {
        val movimientoDao = MovimientoDatabase.getDatabase(application).movimientoDao()
        repository = MovimientoRepository(movimientoDao)
        readAllData = repository.readAllData
    }

    fun readAllData(movimiento: Movimiento): LiveData<List<Movimiento>>{
        return repository.readAllData
    }

    fun readSpecificTimeDataCategoryPayment(categoria: Int, medioPago: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> {
        return repository.readSpecificTimeDataCategoryPayment(categoria, medioPago, tipoMovimiento, desde)
    }

    fun readSpecificTimeDataCategoryAll(medioPago: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> {
        return repository.readSpecificTimeDataCategoryAll(medioPago, tipoMovimiento, desde)
    }

    fun readSpecificTimeDataPaymentAll(categoria: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> {
        return repository.readSpecificTimeDataPaymentAll(categoria, tipoMovimiento, desde)
    }

    fun readSpecificTimeData(tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> {
        return repository.readSpecificTimeData(tipoMovimiento, desde)
    }
}