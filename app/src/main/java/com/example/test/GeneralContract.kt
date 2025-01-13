package com.example.test

interface GeneralContract {
    interface View{
        fun showTasks(tasks: List<TaskItem>)
        fun showError(message: String)
        fun showNewTaskDialog()
        fun showEditTaskDialog(task: TaskItem)
    }

    interface Presenter{
        fun loadTasks()
        fun saveTasks(tasks: List<TaskItem>)
        fun addTask(time: String, taskName: String)
        fun removeTask(task: TaskItem)
        fun updateTask(task: TaskItem)
        fun updateTaskChecked (task: TaskItem, isChecked:Boolean)
        fun clear()
    }
}