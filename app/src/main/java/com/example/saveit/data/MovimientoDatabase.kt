package com.example.saveit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.saveit.model.Movimiento

@Database(entities = [Movimiento::class], version = 2, exportSchema = false)
abstract class MovimientoDatabase : RoomDatabase() {
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var INSTANCE: MovimientoDatabase? = null

        fun getDatabase(context: Context): MovimientoDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovimientoDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}