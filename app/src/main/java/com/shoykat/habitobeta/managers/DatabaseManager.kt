package com.shoykat.habitobeta.managers

import android.content.Context
import com.shoykat.habitobeta.AppDatabase
import com.shoykat.habitobeta.repositories.HabitRepository
import com.shoykat.habitobeta.repositories.TaskLogRepository


class DatabaseManager(context : Context) {
    private val db = AppDatabase.getDatabase(context)
    val habitRepository = HabitRepository(db.habitDao())
    val taskLogRepository = TaskLogRepository(db.taskLogDao())
}