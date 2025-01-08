package com.example.test

class TaskPresenter(
    private val view: GeneralContract.View,
    private val taskManager: TaskManager
) : GeneralContract.Presenter {
    override fun loadTasks() {
        try {
            val tasks = taskManager.loadAllTasks()
            view.showTasks(tasks)
        } catch (e:Exception){
            view.showError("Failed to load tasks")
        }
    }

    override fun saveTasks(tasks: List<TaskItem>) {
        try {
            taskManager.saveAllTasks(tasks)
        } catch (e: Exception) {
            view.showError("Failed to save tasks")
        }
    }

    override fun addTask(task: TaskItem) {
        try {
            taskManager.saveTask(task)
            val tasks = taskManager.loadAllTasks()
            view.showTasks(tasks)
        } catch (e:Exception){
            view.showError("Failed to add new task")
        }
    }

    override fun removeTask(task: TaskItem) {
        try {
            taskManager.removeTask(task)
            val tasks = taskManager.loadAllTasks()
            view.showTasks(tasks)
        } catch (e:Exception){
            view.showError("Failed to remove task")
        }
    }
}