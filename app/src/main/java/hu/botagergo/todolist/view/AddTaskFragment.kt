package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.FragmentAddTaskBinding
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.view_model.TaskListViewModel
import hu.botagergo.todolist.view_model.TaskViewModel
import hu.botagergo.todolist.view_model.TaskViewModelFactory

class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding

    private val viewModel: TaskViewModel by viewModels {
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
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinnerStatus.adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, Task.Status.values()
        )
        binding.spinnerContext.adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, Task.Context.values()
        )

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_task_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == android.R.id.home) or (item.itemId == R.id.menu_item_cancel)) {
            findNavController().popBackStack()
        } else if (item.itemId == R.id.menu_item_done) {
            taskListViewModel.addTask(viewModel.task)
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}