package com.example.saveit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.saveit.data.MovimientoDatabase
import com.example.saveit.repository.MovimientoRepository
import com.example.saveit.model.Movimiento
import com.example.saveit.model.ResultadoReporte
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun readSpecificTimeDataCategoryPayment(moneda: Int, categoria: Int, medioPago: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> {
        return repository.readSpecificTimeDataCategoryPayment(moneda, categoria, medioPago, tipoMovimiento, desde)
    }

    fun readSpecificTimeDataCategoryAll(moneda: Int, medioPago: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> {
        return repository.readSpecificTimeDataCategoryAll(moneda, medioPago, tipoMovimiento, desde)
    }

    fun readSpecificTimeDataPaymentAll(moneda: Int, categoria: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> {
        return repository.readSpecificTimeDataPaymentAll(moneda, categoria, tipoMovimiento, desde)
    }

    fun readSpecificTimeData(moneda: Int, tipoMovimiento: Int, desde: String):
            LiveData<List<ResultadoReporte>> {
        return repository.readSpecificTimeData(moneda, tipoMovimiento, desde)
    }
}