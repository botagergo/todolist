package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.*
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.FragmentTaskListBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.view_model.TaskListViewModel
import hu.botagergo.todolist.adapter.Adapter
import hu.botagergo.todolist.adapter.createAdapter

class TaskListFragment(private val taskListView: Configuration.TaskListView)
    : Fragment() {

    private lateinit var adapter: Adapter
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logd(this, "onCreateView")
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logd(this, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        adapter = createAdapter(this.requireActivity().application as ToDoListApplication, viewModel.tasks.value!!, taskListView)

        adapter.setOnItemDoneClickedListener {
            viewModel.updateTask(it.copy(done = !it.done))
        }

        adapter.setOnItemClickedListener {
            val bundle = bundleOf("uid" to it.uid)
            findNavController().navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle)
        }

        adapter.setOnItemDeleteClickedListener {
            viewModel.deleteTask(it)
        }
        val itemTouchHelper = adapter.getItemTouchHelper()
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter.refresh()
    }

}