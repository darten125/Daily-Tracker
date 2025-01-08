package com.example.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ListItemBinding

class TaskAdapter(private var itemList: List<TaskItem>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder для одного элемента списка
    class TaskViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Инфлейтим разметку с помощью viewBinding
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.clockTime.text = item.time
        holder.binding.medicineName.text = item.taskName
        holder.binding.itemCheckbox.isChecked = item.checked
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // Метод для обновления списка задач
    fun updateTasks(newTasks: List<TaskItem>) {
        itemList = newTasks
        notifyDataSetChanged() // Обновляем RecyclerView
    }
}

