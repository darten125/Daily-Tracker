package com.example.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var medicineManager: MedicineManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализация binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        medicineManager = MedicineManager(this)

        // Установка корневого представления
        setContentView(binding.root)

        // Пример данных для списка
        /*val medicines = listOf(
            MedicineItem("08:00", "Парацетамол", true),
            MedicineItem("12:00", "Ибупрофен", false),
            MedicineItem("18:00", "Аспирин",true)
        )*/

        val medicineList = medicineManager.loadMedicines()

        // Настраиваем RecyclerView
        val adapter = MedicineAdapter(medicineList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}