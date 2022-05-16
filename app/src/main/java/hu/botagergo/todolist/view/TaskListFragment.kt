package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.TouchCallback
import hu.botagergo.todolist.R
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.adapter.task_list.*
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.FragmentTaskListBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.TaskView
import hu.botagergo.todolist.sorter.ManualTaskSorter
import hu.botagergo.todolist.view_model.TaskListViewModel
import java.util.*

class TaskListFragment
    : Fragment() {

    val app: ToDoListApplication by lazy {
        requireActivity().application as ToDoListApplication
    }

    private lateinit var adapter: TaskListAdapter
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by activityViewModels()
    private lateinit var viewUuid: UUID

    private val taskListView: TaskView by lazy {
        config.taskViews[viewUuid]!!
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

        adapter = if (taskListView.grouper != null) {
            GroupedTaskListAdapter(this.requireActivity().application as ToDoListApplication,
                config.state.taskGroupExpanded.getOrPut(taskListView.uuid) { mutableMapOf() })
        } else {
            SimpleTaskListAdapter(this.requireActivity().application as ToDoListApplication)
        }

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

        ItemTouchHelper(SimpleTouchCallback(taskListView.sorter as? ManualTaskSorter)).attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)

        if (viewModel.tasks.size != 0 ) {
            lifecycleScope.launch {
                adapter.tasks = taskListView.apply(viewModel.tasks).toMutableList()
            }
        }

        app.taskDataSetChangedEvent.subscribe {
            lifecycleScope.launch {
                adapter.tasks = taskListView.apply(viewModel.tasks).toMutableList()
            }
        }
    }

    class SimpleTouchCallback(private val manualTaskSorter: ManualTaskSorter?) : TouchCallback() {
        override fun onMove(
            recyclerView: RecyclerView, source: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val adapter = recyclerView.adapter as TaskListAdapter
            adapter.moveItem(source.bindingAdapterPosition, target.bindingAdapterPosition)

            if (manualTaskSorter != null) {
                val fromTask = adapter.taskAt(source.bindingAdapterPosition) ?: return true
                val toTask = adapter.taskAt(target.bindingAdapterPosition) ?: return true

                val fromInd = manualTaskSorter.uids.indexOfFirst { it == fromTask.uid }
                val toInd = manualTaskSorter.uids.indexOfFirst { it == toTask.uid }

                if (fromInd == -1 || toInd == -1) {
                    return true
                }

                Collections.swap(manualTaskSorter.uids, fromInd, toInd)
            }

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            throw NotImplementedError()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(arguments)
        super.onSaveInstanceState(outState)
    }
}