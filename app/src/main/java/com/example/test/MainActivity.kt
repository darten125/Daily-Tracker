package com.example.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GeneralContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var medicineManager: TaskManager

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
        adapter = TaskAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Пример данных для списка
        /*val tasks = listOf(
            TaskItem("1","08:00", "Парацетамол", true),
            TaskItem("2","12:00", "Ибупрофен", false),
            TaskItem("3","18:00", "Аспирин",true)
        )

        presenter.saveTasks(tasks)*/
        presenter.loadTasks()
    }

    override fun showTasks(tasks: List<TaskItem>) {
        adapter.updateTasks(tasks)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}