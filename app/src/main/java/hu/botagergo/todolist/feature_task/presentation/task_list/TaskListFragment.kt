package hu.botagergo.todolist.feature_task.presentation.task_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.TouchCallback
import dagger.hilt.android.AndroidEntryPoint
import hu.botagergo.todolist.*
import hu.botagergo.todolist.databinding.FragmentTaskListBinding
import hu.botagergo.todolist.core.log.logd
import hu.botagergo.todolist.feature_task_view.data.model.TaskView
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import hu.botagergo.todolist.feature_task_view.data.sorter.ManualTaskSorter
import hu.botagergo.todolist.feature_task.domain.use_case.TaskUseCase
import hu.botagergo.todolist.feature_task.presentation.task_list.adapter.GroupedTaskListAdapter
import hu.botagergo.todolist.feature_task.presentation.task_list.adapter.SimpleTaskListAdapter
import hu.botagergo.todolist.feature_task.presentation.task_list.adapter.TaskListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TaskListFragment
    : Fragment() {

    @Inject lateinit var taskViewRepo: TaskViewRepository
    @Inject lateinit var taskUseCase: TaskUseCase

    val app: ToDoListApplication by lazy {
        requireActivity().application as ToDoListApplication
    }

    private lateinit var adapter: TaskListAdapter
    private lateinit var binding: FragmentTaskListBinding

    private lateinit var viewUuid: UUID
    private lateinit var taskView: TaskView

    override fun onCreate(savedInstanceState: Bundle?) {
        viewUuid = savedInstanceState?.get(EXTRA_UUID) as? UUID
            ?: arguments?.get(EXTRA_UUID) as UUID
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

        taskView = taskViewRepo.get(viewUuid)

        adapter = taskView.grouper?.let {
            GroupedTaskListAdapter(this.requireActivity().application as ToDoListApplication,
                config.state.taskGroupExpanded.getOrPut(taskView.uuid) { mutableMapOf() })
        } ?: SimpleTaskListAdapter(this.requireActivity().application as ToDoListApplication)


        adapter.setOnItemDoneClickedListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    taskUseCase.updateTask(it.copy(done = !it.done))
                }
            }
        }

        adapter.setOnItemClickedListener {
            findNavController().navigate(
                R.id.action_taskListFragment_to_taskFragment,
                bundleOf(
                    EXTRA_UID to it.uid,
                    EXTRA_IS_EDIT to true
                )
            )
        }

        adapter.setOnItemDeleteClickedListener {
            lifecycleScope.launch {
                taskUseCase.deleteTask(it)
            }
        }

        ItemTouchHelper(SimpleTouchCallback(taskView.sorter as? ManualTaskSorter)).attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)

        lifecycleScope.launch {
            taskUseCase.getTaskGroups(taskView).collectLatest {
                adapter.tasks = it.toMutableList()
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

            manualTaskSorter?.let { sorter ->
                val fromTask = adapter.taskAt(source.bindingAdapterPosition) ?: return true
                val toTask = adapter.taskAt(target.bindingAdapterPosition) ?: return true

                val fromInd = sorter.uids.indexOfFirst { it == fromTask.uid }
                val toInd = sorter.uids.indexOfFirst { it == toTask.uid }

                if (fromInd == -1 || toInd == -1) {
                    return true
                }

                Collections.swap(sorter.uids, fromInd, toInd)
            }

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            throw NotImplementedError()
        }
    }

    override fun onStart() {
        val newTaskView = taskViewRepo.get(viewUuid)
        if (newTaskView != taskView) {
            lifecycleScope.launchWhenStarted {
                adapter.tasks = taskUseCase.getTaskGroups(newTaskView).last().toMutableList()
                taskView = newTaskView
            }
        }
        super.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(arguments)
        super.onSaveInstanceState(outState)
    }
}