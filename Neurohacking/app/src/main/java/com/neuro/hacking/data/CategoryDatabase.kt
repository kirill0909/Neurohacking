package com.neuro.hacking.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database
import com.neuro.hacking.model.Category

@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class CategoryDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: CategoryDatabase? = null
        fun getDatabase(context: Context): CategoryDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CategoryDatabase::class.java,
                    "neurohacking_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}