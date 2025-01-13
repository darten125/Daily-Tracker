package com.example.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GeneralContract.View {

    private lateinit var binding: ActivityMainBinding

    private lateinit var presenter: GeneralContract.Presenter
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализация binding
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Установка корневого представления
        setContentView(binding.root)

        // Инициализация Presenter
        presenter = TaskPresenter(this, TaskManager(this))
        adapter = TaskAdapter(
            mutableListOf(),
            onTaskCheckedChanged = { task, isChecked ->
            presenter.updateTaskChecked(task, isChecked) },
            onTaskEdit = { task -> showEditTaskDialog(task) },
            onTaskDelete = { task -> presenter.removeTask(task) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        //presenter.clear()
        presenter.loadTasks()

        binding.addButton.setOnClickListener {
            showNewTaskDialog()
        }
    }

    override fun showTasks(tasks: List<TaskItem>) {
        adapter.updateTasks(tasks)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showNewTaskDialog() {
        val dialog = NewTaskDialogFragment { time, taskName ->
            presenter.addTask(time, taskName)
        }
        dialog.show(supportFragmentManager, "NewTaskDialog")
    }

    override fun showEditTaskDialog(task:TaskItem) {
        val dialog = NewTaskDialogFragment { updatedTime, updatedTaskName ->
            presenter.updateTask(task.copy(time = updatedTime, taskName = updatedTaskName))
        }
        // Передаём текущие значения времени и имени задачи в диалог
        dialog.arguments = Bundle().apply {
            putString("time", task.time)
            putString("taskName", task.taskName)
        }
        dialog.show(supportFragmentManager, "EditTaskDialog")
    }

}