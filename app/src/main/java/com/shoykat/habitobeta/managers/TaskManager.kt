package com.shoykat.habitobeta.managers

import androidx.lifecycle.MutableLiveData
import com.shoykat.habitobeta.managers.DatabaseManager
import com.shoykat.habitobeta.models.HabitWithTaskLogs
import com.shoykat.habitobeta.models.TaskLog
import com.shoykat.habitobeta.models.TaskModel
import com.shoykat.habitobeta.utils.CalendarUtil
import com.shoykat.habitobeta.utils.TaskUtil

class TaskManager(private val databaseManager: DatabaseManager) {

    val tasks : MutableLiveData<ArrayList<TaskModel>> by lazy {
        MutableLiveData<ArrayList<TaskModel>>()
    }

    fun generateDailyTasks(habits : List<HabitWithTaskLogs>) {
        val taskList = ArrayList<TaskModel>()

        val currentTimestamp = System.currentTimeMillis()

        for(habitWithTaskLogs in habits) {
            if(habitWithTaskLogs.habit.disabled) continue
            
            if(CalendarUtil.isHabitScheduledForToday(habitWithTaskLogs.habit)) {

                //Check if already added a task log for habit today. If already has a task log for today, don't add the task
                if (!TaskUtil.hasTaskLogForDate(habitWithTaskLogs, currentTimestamp)) {
                    taskList.add(TaskModel(habitWithTaskLogs))
                }
            }
        }

        tasks.value = taskList
    }

    suspend fun insertTaskLog(taskModel: TaskModel, taskStatus : String, timestamp: Long) {
        val taskLog = TaskLog()

        val habit = taskModel.habitWithTaskLogs.habit

        taskLog.habitId = habit.id

        taskLog.timestamp = timestamp
        taskLog.status = taskStatus

        when(taskStatus) {
            TaskUtil.STATUS_SUCCESS -> {
                //Increase habit score
                val oldScore = habit.score
                val newScore = oldScore + 1

                habit.score = newScore
                taskLog.score = newScore
            }

            TaskUtil.STATUS_FAILED -> {
                //Reset habit score
                val newScore = 0

                habit.score = newScore
                taskLog.score = newScore
            }
        }


        databaseManager.taskLogRepository.createTaskLog(taskLog)

        databaseManager.habitRepository.updateHabit(habit)
    }
}