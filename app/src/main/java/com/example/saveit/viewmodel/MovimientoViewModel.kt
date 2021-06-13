package com.example.saveit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.saveit.data.MovimientoDatabase
import com.example.saveit.repository.MovimientoRepository
import com.example.saveit.model.Movimiento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovimientoViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Movimiento>>
    private val repository: MovimientoRepository

    init {
        val movimientoDao = MovimientoDatabase.getDatabase(application).movimientoDao()
        repository = MovimientoRepository(movimientoDao)
        readAllData = repository.readAllData
    }

    fun readAllData(movimiento: Movimiento): LiveData<List<Movimiento>>{
        return repository.readAllData
    }

    fun readIngresos(desde: Long, hasta: Long): LiveData<Double> {
        return repository.readIngresos(desde, hasta)
    }

    fun readGastos(desde: Long, hasta: Long): LiveData<Double> {
        return repository.readGastos(desde, hasta)
    }

    fun addMovimiento(movimiento: Movimiento){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovimiento(movimiento)
        }
    }

    fun updateMovimiento(movimiento: Movimiento){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMovimiento(movimiento)
        }
    }
}