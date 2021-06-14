package com.example.saveit.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.saveit.model.Movimiento

@Dao
interface MovimientoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovimiento(movimiento: Movimiento)

    @Update
    suspend fun updateMovimiento(movimiento: Movimiento)

    @Query("SELECT * FROM movimiento_table ORDER BY fecha DESC")
    fun readAllData(): LiveData<List<Movimiento>>

    @Query("SELECT * FROM movimiento_table WHERE fecha BETWEEN :desde AND :hasta ORDER BY fecha DESC")
    fun readAllDataBetween(desde: Long, hasta: Long): LiveData<List<Movimiento>>

    @Query("SELECT SUM(monto) FROM movimiento_table WHERE tipoMovimiento = 0 AND fecha BETWEEN :desde AND :hasta")
    fun readIngresos(desde: Long, hasta: Long): LiveData<Double>

    @Query("SELECT SUM(monto) FROM movimiento_table WHERE tipoMovimiento = 1 AND fecha BETWEEN :desde AND :hasta")
    fun readGastos(desde: Long, hasta: Long): LiveData<Double>
}