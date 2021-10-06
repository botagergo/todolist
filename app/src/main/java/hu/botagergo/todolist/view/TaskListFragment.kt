package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.R
import hu.botagergo.todolist.TaskView
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.adapter.Adapter
import hu.botagergo.todolist.adapter.createAdapter
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.FragmentTaskListBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.view_model.TaskListViewModel
import java.util.*

class TaskListFragment
    : Fragment() {

    private lateinit var adapter: Adapter
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by activityViewModels()
    private lateinit var viewUuid: UUID

    private val taskListView: TaskView by lazy {
        config.taskViews.find {
            it.uuid == viewUuid
        }!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        var uuid = savedInstanceState?.get("uuid") as? UUID
        if (uuid == null) {
            uuid = arguments?.get("uuid") as UUID
        }
        viewUuid = uuid
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

        adapter = createAdapter(
            this.requireActivity().application as ToDoListApplication,
            viewModel.tasks.value!!,
            taskListView
        )

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
        itemTouchHelper?.attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter.refresh()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(arguments)
        super.onSaveInstanceState(outState)
    }

}