package com.neuro.hacking.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database
import com.neuro.hacking.model.Category
import com.neuro.hacking.model.Word

@Database(entities = [Category::class, Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun wordDao(): WordDao

    /*
    var MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE word_table (id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, translation TEXT)")
        }

    }
     */

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neurohacking_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}