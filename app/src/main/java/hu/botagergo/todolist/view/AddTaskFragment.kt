package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hu.botagergo.todolist.R
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.databinding.FragmentAddTaskBinding
import hu.botagergo.todolist.view_model.TaskListViewModel
import hu.botagergo.todolist.view_model.TaskViewModel
import hu.botagergo.todolist.view_model.TaskViewModelFactory

class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(requireActivity().application, 0)
    }

    private val taskListViewModel: TaskListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_task,
            container, false)
        binding.lifecycleOwner = this
        binding.viewModel = taskViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinnerStatus.adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, Task.Status.values())
        binding.spinnerContext.adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, Task.Context.values())

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_task_fragment_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == android.R.id.home) or (item.itemId == R.id.menu_item_cancel)) {
            findNavController().popBackStack()
        } else if (item.itemId == R.id.menu_item_done) {
            taskListViewModel.addTask(taskViewModel.task)
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}