package com.example.saveit.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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

    fun addMovimiento(movimiento: Movimiento){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovimiento(movimiento)
        }
    }
}