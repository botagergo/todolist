package com.example.taskmanager

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.taskmanager.databinding.FragmentAddTaskBinding

class AddTaskFragment : Fragment() {

    interface TaskListener {
        fun onAddTaskResult(task: Task)
    }

    private var listener: TaskListener? = null
    private lateinit var binding: FragmentAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        listener = context as TaskListener
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinnerStatus.adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, Task.Status.values())
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onAddTaskResult() {
        val task = Task(binding.editTextTitle.text.toString(),
            binding.editTextComments.text.toString(),
            binding.spinnerStatus.selectedItem as Task.Status)
        listener?.onAddTaskResult(task)
        findNavController().popBackStack()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        inflater.inflate(R.menu.add_task_fragment_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == android.R.id.home) or (item.itemId == R.id.menu_item_cancel)) {
            activity?.onBackPressed()
        } else if (item.itemId == R.id.menu_item_done) {
            onAddTaskResult()
        }
        return super.onOptionsItemSelected(item)
    }
}