package com.example.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ListItemBinding

// Адаптер для вашего списка
class MedicineAdapter(private val itemList: List<MedicineItem>) :
    RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    // ViewHolder для одного элемента списка
    class MedicineViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        // Инфлейтим разметку с помощью viewBinding
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MedicineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.clockTime.text = item.time
        holder.binding.medicineName.text = item.medicineName
        if (item.checked)
            holder.binding.itemCheckbox.isChecked = true
        else
            holder.binding.itemCheckbox.isChecked = false
        // Дополнительно можно обработать CheckBox и ImageButton, если нужно
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}