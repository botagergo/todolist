package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.taskmanager.databinding.ActivityTaskBinding

const val EXTRA_TASK_TITLE = "com.example.taskmanager.TASK_TITLE"
const val EXTRA_TASK_COMMENTS = "com.example.taskmanager.TASK_COMMENTS"
const val EXTRA_TASK_UID = "com.example.taskmanager.EXTRA_TASK_UID"
const val EXTRA_TASK_STATUS = "com.example.taskmanager.EXTRA_TASK_STATUS"

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private var uid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setResult(RESULT_CANCELED)
        binding.spinnerStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Task.Status.values())

        val extras = intent.extras
        binding.editTextTitle.setText((extras?.get(EXTRA_TASK_TITLE) ?: String()) as String)
        binding.editTextComments.setText((extras?.get(EXTRA_TASK_COMMENTS) ?: String()) as String)

        val status = (extras?.get(EXTRA_TASK_STATUS) ?: Task.Status.None) as Task.Status
        val spinnerAdapter = binding.spinnerStatus.adapter as ArrayAdapter<Task.Status>
        binding.spinnerStatus.setSelection(spinnerAdapter.getPosition(status))

        uid = (extras?.get(EXTRA_TASK_UID) ?: 0) as Int
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }

    fun onClickButton(view: View) {
        if (view.id == R.id.button) {
            Log.d(TAG, binding.editTextTitle.text.toString())
            val intent = Intent().apply {
                putExtra(EXTRA_TASK_TITLE, binding.editTextTitle.text.toString())
                putExtra(EXTRA_TASK_COMMENTS, binding.editTextComments.text.toString())
                putExtra(EXTRA_TASK_STATUS, binding.spinnerStatus.selectedItem as Task.Status)
                putExtra(EXTRA_TASK_UID, uid)
            }

            Log.d(TAG, intent.getStringExtra(EXTRA_TASK_TITLE)!!)

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}