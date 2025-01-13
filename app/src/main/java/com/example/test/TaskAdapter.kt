package com.example.test

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ListItemBinding

class TaskAdapter(
    private var itemList: List<TaskItem>,
    private val onTaskCheckedChanged: (TaskItem,Boolean)-> Unit,
    private val onTaskEdit:(TaskItem) -> Unit,
    private val onTaskDelete:(TaskItem) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

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

        holder.binding.itemCheckbox.setOnCheckedChangeListener{ _,isChecked ->
            onTaskCheckedChanged(item, isChecked)
        }

        holder.binding.taskOptions.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, holder.binding.taskOptions)
            popupMenu.inflate(R.menu.options_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        onTaskEdit(item) // Вызываем callback для редактирования
                        true
                    }
                    R.id.action_delete -> {
                        onTaskDelete(item) // Вызываем callback для удаления
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // Метод для обновления списка задач
    fun updateTasks(newTasks: List<TaskItem>) {
        itemList = newTasks
        notifyDataSetChanged() // Обновляем RecyclerView
    }

    fun getItemList(): List<TaskItem>{
        return itemList
    }
}

