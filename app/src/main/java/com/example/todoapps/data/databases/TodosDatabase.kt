package com.example.todoapps.data.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapps.data.dao.TodosDao
import com.example.todoapps.data.entity.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodosDatabase : RoomDatabase() {
    abstract val todosDao: TodosDao

    companion object {
        private var INSTANCE: TodosDatabase? = null

        fun getInstance(context: Context): TodosDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, TodosDatabase::class.java, "todo_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }


}