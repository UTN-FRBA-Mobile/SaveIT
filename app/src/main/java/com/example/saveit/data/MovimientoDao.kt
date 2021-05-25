package com.example.saveit.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovimientoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovimiento(movimiento: Movimiento)

    @Query("SELECT * FROM movimiento_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Movimiento>>
}