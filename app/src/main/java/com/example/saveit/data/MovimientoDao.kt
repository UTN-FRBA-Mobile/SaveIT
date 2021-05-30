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

    @Query("SELECT * FROM movimiento_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Movimiento>>
}