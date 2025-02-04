package com.example.test

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.test.databinding.FragmentNewTaskDialogBinding

class NewTaskDialogFragment(
    private val onTaskDataEntered: (String, String) -> Unit
) : DialogFragment() {

    private var _binding: FragmentNewTaskDialogBinding? = null
    private val binding get() = _binding!!

    private var selectedTime: String? = null
    private var initialTaskName: String? = null
    private var initialTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            initialTaskName = it.getString("taskName")
            initialTime = it.getString("time")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentNewTaskDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT)

        binding.newTaskNameInput.setText(initialTaskName) // Устанавливаем имя задачи
        binding.newTaskTimeInput.text = initialTime // Устанавливаем время
        selectedTime = initialTime // Запоминаем время

        binding.newTaskTimeInput.setOnClickListener {
            binding.newTaskTimeInput.setTextColor(resources.getColor(R.color.black))
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    binding.newTaskTimeInput.text = selectedTime
                },
                hour,
                minute,
                true // Использовать 24-часовой формат
            ).show()
        }

        binding.createButton.setOnClickListener {
            val taskName = binding.newTaskNameInput.text.toString()
            //val taskTime = binding.newTaskTimeInput.text.toString()
            if(taskName.isBlank() || selectedTime.isNullOrBlank()){
                Toast.makeText(context,"Feel all data!", Toast.LENGTH_SHORT).show()
            }
            else{
                onTaskDataEntered(selectedTime!!,taskName)
                dismiss()
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}