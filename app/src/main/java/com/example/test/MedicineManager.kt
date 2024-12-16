package com.example.test
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MedicineManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("medicine_preferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val MEDICINE_LIST_KEY = "medicine_list_key"

    // Сохранение списка лекарств
    fun saveMedicines(medicineList: List<MedicineItem>) {
        val jsonString = gson.toJson(medicineList) // Преобразуем список в JSON
        sharedPreferences.edit().putString(MEDICINE_LIST_KEY, jsonString).apply() // Сохраняем JSON строку
    }

    // Загрузка списка лекарств
    fun loadMedicines(): List<MedicineItem> {
        val jsonString = sharedPreferences.getString(MEDICINE_LIST_KEY, null) // Получаем JSON строку
        return if (jsonString != null) {
            val type = object : TypeToken<List<MedicineItem>>() {}.type
            gson.fromJson(jsonString, type) // Преобразуем JSON обратно в список объектов MedicineItem
        } else {
            emptyList() // Если данных нет, возвращаем пустой список
        }
    }
}