package com.example.saveit.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.saveit.model.Movimiento
import com.example.saveit.model.ResultadoReporte

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

    @Query("SELECT * FROM movimiento_table WHERE categoria = 1 ORDER BY fecha DESC")
    fun readAhorros(): LiveData<List<Movimiento>>

    @Query("SELECT * FROM movimiento_table WHERE categoria = 1 AND fecha BETWEEN :desde AND :hasta ORDER BY fecha DESC")
    fun readAhorrosBetween(desde: Long, hasta: Long): LiveData<List<Movimiento>>

    @Query("SELECT SUM(monto) FROM movimiento_table WHERE tipoMovimiento = 0 AND fecha BETWEEN :desde AND :hasta")
    fun readIngresos(desde: Long, hasta: Long): LiveData<Double>

    @Query("SELECT SUM(monto) FROM movimiento_table WHERE tipoMovimiento = 1 AND fecha BETWEEN :desde AND :hasta")
    fun readGastos(desde: Long, hasta: Long): LiveData<Double>

    @Query("SELECT strftime('%s', strftime('%Y-%m-%d', datetime(fecha/1000, 'unixepoch'))) as Day, SUM(monto) as Value " +
            "FROM movimiento_table " +
            "WHERE moneda = :moneda " +
            "AND categoria = :categoria " +
            "AND medioDePago = :medioDePago " +
            "AND tipoMovimiento = :tipoMovimiento " +
            "AND fecha >= strftime('%s', DATETIME('NOW', :desde)) * 1000 " +
            "GROUP BY day " +
            "ORDER BY day asc")
    fun readSpecificTimeDataCategoryPayment(moneda: Int,
                                            categoria: Int,
                                            medioDePago: Int,
                                            tipoMovimiento: Int,
                                            desde: String): LiveData<List<ResultadoReporte>>

    @Query("SELECT strftime('%s', strftime('%Y-%m-%d', datetime(fecha/1000, 'unixepoch'))) as Day, SUM(monto) as Value " +
            "FROM movimiento_table " +
            "WHERE moneda = :moneda " +
            "AND medioDePago = :medioDePago " +
            "AND tipoMovimiento = :tipoMovimiento " +
            "AND fecha >= strftime('%s', DATETIME('NOW', :desde)) * 1000 " +
            "GROUP BY day " +
            "ORDER BY day asc")
    fun readSpecificTimeDataCategoryAll(moneda: Int,
                                        medioDePago: Int,
                                        tipoMovimiento: Int,
                                        desde: String): LiveData<List<ResultadoReporte>>

    @Query("SELECT strftime('%s', strftime('%Y-%m-%d', datetime(fecha/1000, 'unixepoch'))) as Day, SUM(monto) as Value " +
            "FROM movimiento_table " +
            "WHERE moneda = :moneda " +
            "AND categoria = :categoria " +
            "AND tipoMovimiento = :tipoMovimiento " +
            "AND fecha >= strftime('%s', DATETIME('NOW', :desde)) * 1000 " +
            "GROUP BY day " +
            "ORDER BY day asc")
    fun readSpecificTimeDataPaymentAll(moneda: Int,
                                       categoria: Int,
                                       tipoMovimiento: Int,
                                       desde: String): LiveData<List<ResultadoReporte>>

    @Query("SELECT strftime('%s', strftime('%Y-%m-%d', datetime(fecha/1000, 'unixepoch'))) as Day, SUM(monto) as Value " +
            "FROM movimiento_table " +
            "WHERE moneda = :moneda " +
            "AND tipoMovimiento = :tipoMovimiento " +
            "AND fecha >= strftime('%s', DATETIME('NOW', :desde)) * 1000 " +
            "GROUP BY day " +
            "ORDER BY day asc")
    fun readSpecificTimeData(moneda: Int,
                             tipoMovimiento: Int,
                             desde: String): LiveData<List<ResultadoReporte>>
}