package com.example.test
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.Calendar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("task_preferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val TASK_IDS_KEY = "task_ids"// Ключ для хранения списка идентификаторов
    private val AVAILABLE_IDS_KEY = "available_ids" // Ключ для хранения свободных ID
    private val CURRENT_MAX_ID_KEY = "current_max_id" // Ключ для хранения текущего максимального ID

    fun clear(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    // Добавление задачи
    fun addTask(task: TaskItem) {
        val taskIds = loadTaskIds().toMutableSet()
        taskIds.add(task.id)
        sharedPreferences.edit()
            .putStringSet(TASK_IDS_KEY, taskIds)
            .putString(task.id, gson.toJson(task))
            .apply()
    }

    // Генерация уникального ID для новой задачи
    fun generateTaskId(): String {
        val availableIds = loadAvailableIds().toMutableSet()
        val editor = sharedPreferences.edit()

        return if (availableIds.isNotEmpty()) {
            // Используем первый доступный ID
            val id = availableIds.first()
            availableIds.remove(id)
            editor.putStringSet(AVAILABLE_IDS_KEY, availableIds.map { it.toString() }.toSet())
            editor.apply()
            id.toString()
        } else {
            // Генерируем новый ID
            val currentMaxId = sharedPreferences.getInt(CURRENT_MAX_ID_KEY, 0)
            val newId = currentMaxId + 1
            editor.putInt(CURRENT_MAX_ID_KEY, newId)
            editor.apply()
            newId.toString()
        }
    }

    fun saveTaskChanges(task: TaskItem){
        sharedPreferences.edit()
            .putString(task.id, gson.toJson(task))
            .apply()
    }

    // Удаление задачи
    fun removeTask(task: TaskItem) {
        val taskIds = loadTaskIds().toMutableSet()
        if (taskIds.remove(task.id)) {
            val availableIds = loadAvailableIds().toMutableSet()
            availableIds.add(task.id.toInt())
            sharedPreferences.edit()
                .putStringSet(TASK_IDS_KEY, taskIds) // Обновляем список идентификаторов
                .putStringSet(AVAILABLE_IDS_KEY, availableIds.map { it.toString() }.toSet()) // Сохраняем свободные ID
                .remove(task.id)
                .apply()
        }
    }

    // Загрузка задачи по ID
    fun loadTask(taskId: String): TaskItem? {
        val jsonString = sharedPreferences.getString(taskId, null)
        return if (jsonString != null) {
            gson.fromJson(jsonString, TaskItem::class.java)
        } else {
            null
        }
    }

    // Загрузка всех задач
    fun loadAllTasks(): List<TaskItem> {
        val taskIds = loadTaskIds()
        return (taskIds.mapNotNull { loadTask(it) }).sortedBy { it.time }
    }

    // Сохранение всех задач
    fun saveAllTasks(taskList: List<TaskItem>) {
        val taskIds = taskList.map { it.id }.toSet()
        val editor = sharedPreferences.edit()
        for (task in taskList) {
            editor.putString(task.id, gson.toJson(task))
        }
        editor.putStringSet(TASK_IDS_KEY, taskIds)
        editor.apply()
    }


    // Загрузка идентификаторов задач
    private fun loadTaskIds(): Set<String> {
        return sharedPreferences.getStringSet(TASK_IDS_KEY, emptySet()) ?: emptySet()
    }

    private fun loadAvailableIds(): Set<Int> {
        return sharedPreferences.getStringSet(AVAILABLE_IDS_KEY, emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet() ?: emptySet()
    }

    fun setAlarmForTask(context: Context, task: TaskItem) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("taskName", task.taskName)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val timeParts = task.time.split(":").map { it.toInt() }
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timeParts[0])
            set(Calendar.MINUTE, timeParts[1])
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1) // Если время уже прошло, ставим на следующий день
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
