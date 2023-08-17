package com.shoykat.habitobeta

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shoykat.habitobeta.dao.HabitDao
import com.shoykat.habitobeta.dao.TaskLogDao
import com.shoykat.habitobeta.models.Habit
import com.shoykat.habitobeta.models.TaskLog

@Database(entities = [Habit::class, TaskLog::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao() : HabitDao
    abstract fun taskLogDao() : TaskLogDao

    companion object {
        private const val DATABASE_NAME = "database_app"
        const val DATABASE_LOG_TAG = "database_log"

        @Volatile
        private var DATABASE_INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            return DATABASE_INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME)
                        .build()

                DATABASE_INSTANCE = instance

                return@synchronized instance
            }
        }
    }
}