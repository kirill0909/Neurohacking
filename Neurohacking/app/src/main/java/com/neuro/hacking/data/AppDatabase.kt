package com.neuro.hacking.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.neuro.hacking.model.Category
import com.neuro.hacking.model.Word
import android.util.Log

@Database(entities = [Category::class, Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun wordDao(): WordDao


    companion object {
        /*
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("")
                Log.d("AppDatabase", "The database has been update")
            }
        }
         */
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
                    .createFromAsset("database/AppDatabase")
                    //.addMigrations()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}