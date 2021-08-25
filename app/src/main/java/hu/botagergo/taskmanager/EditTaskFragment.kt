package hu.botagergo.taskmanager

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import hu.botagergo.taskmanager.databinding.FragmentEditTaskBinding

class EditTaskFragment : Fragment() {

    interface Listener {
        fun onEditTaskResult(task: Task)
    }

    private var listener: Listener? = null
    private lateinit var adapter: ArrayAdapter<Task.Status>
    private lateinit var binding: FragmentEditTaskBinding
    private lateinit var task: Task

    private fun setTask(task: Task) {
        this.task = task
        binding.editTextTitle.setText(task.title)
        binding.editTextComments.setText(task.comments)
        binding.spinnerStatus.setSelection(adapter.getPosition(task.status))
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
        binding = FragmentEditTaskBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, Task.Status.values())
        binding.spinnerStatus.adapter = adapter
        setTask(arguments?.getParcelable("task") ?: Task())
        super.onViewCreated(view, savedInstanceState)
    }


    private fun onEditTaskResult() {
        val task = Task(binding.editTextTitle.text.toString(),
            binding.editTextComments.text.toString(),
            binding.spinnerStatus.selectedItem as Task.Status)
        listener?.onEditTaskResult(task)
        findNavController().popBackStack()
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
}