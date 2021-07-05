package com.example.todoapp.Data

import android.content.Context
import androidx.room.*
import com.example.todoapp.Data.Models.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun ToDoDao(): ToDoDao


    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance

                return instance
            }
        }
    }
}