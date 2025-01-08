package com.example.test
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("task_preferences", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val TASK_IDS_KEY = "task_ids"// Ключ для хранения списка идентификаторов

    // Сохранение задачи
    fun saveTask(task: TaskItem) {
        val taskIds = loadTaskIds().toMutableSet()
        taskIds.add(task.id)
        sharedPreferences.edit()
            .putStringSet(TASK_IDS_KEY, taskIds)
            .putString(task.id, gson.toJson(task))
            .apply()
    }

    // Удаление задачи
    fun removeTask(task: TaskItem) {
        val taskIds = loadTaskIds().toMutableSet()
        if (taskIds.remove(task.id)) {
            sharedPreferences.edit()
                .putStringSet(TASK_IDS_KEY, taskIds)
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
        return taskIds.mapNotNull { loadTask(it) }
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

}