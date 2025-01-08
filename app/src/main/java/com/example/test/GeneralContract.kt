package com.example.test

interface GeneralContract {
    interface View{
        fun showTasks(tasks: List<TaskItem>)
        fun showError(message: String)
    }

    interface Presenter{
        fun loadTasks()
        fun saveTasks(tasks: List<TaskItem>)
        fun addTask(task: TaskItem)
        fun removeTask(task: TaskItem)
    }
}