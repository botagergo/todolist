package hu.botagergo.taskmanager.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import hu.botagergo.taskmanager.R
import hu.botagergo.taskmanager.model.Task
import hu.botagergo.taskmanager.databinding.FragmentEditTaskBinding
import hu.botagergo.taskmanager.view_model.TaskViewModel
import hu.botagergo.taskmanager.view_model.TaskViewModelFactory

class EditTaskFragment : Fragment() {

    interface Listener {
        fun onEditTaskResult(task: Task)
    }
    private var listener: Listener? = null

    private lateinit var binding: FragmentEditTaskBinding

    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(requireActivity().application, arguments?.getLong("uid") ?: 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        listener = context as Listener
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_task,
            container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinnerStatus.adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, Task.Status.values())
        binding.spinnerContext.adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, Task.Context.values())

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        inflater.inflate(R.menu.edit_task_fragment_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == android.R.id.home) or (item.itemId == R.id.menu_item_cancel)) {
            activity?.onBackPressed()
        } else if (item.itemId == R.id.menu_item_done) {
            onEditTaskResult()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onEditTaskResult() {
        listener?.onEditTaskResult(viewModel.task)
        findNavController().popBackStack()
    }
}