package hu.botagergo.todolist.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.*
import hu.botagergo.todolist.adapter.*
import hu.botagergo.todolist.databinding.FragmentTaskListBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import hu.botagergo.todolist.view_model.TaskListViewModel
import java.util.*

class TaskListFragment(private val taskListView: Configuration.TaskListView) : Fragment() {
    private lateinit var  itemTouchHelper: ItemTouchHelper
    private lateinit var adapter: Adapter
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        val activity = requireActivity()
        adapter =
            if (taskListView.grouper.value != null )
                TaskGroupListAdapter(activity, viewModel.getTasks().value!!, viewModel, taskListView)
            else
                TaskListAdapter(activity, viewModel.getTasks().value!!, viewModel, taskListView, null)

        taskListView.filter.observe(requireActivity()) {
            adapter.refreshAll()
        }
        taskListView.grouper.observe(requireActivity()) {
            adapter.refreshAll()
        }

        taskListView.sorter.observe(requireActivity()) {
            adapter.refreshAll()
        }

        adapter.listener = object : Adapter.Listener {
            override fun onDoneOrUndoneClicked(task: Task, done: Boolean) {
                logd(this, "onDoneClicked")
                viewModel.updateTask(task.copy(done = done))
                adapter.refreshAll()
            }

            override fun onDeleteClicked(task: Task) {
                logd(this, "onDeleteClicked")
                viewModel.deleteTask(task)
                adapter.refreshAll()
            }

            override fun onTaskClicked(task: Task) {
                val bundle = bundleOf("uid" to task.uid)
                findNavController().navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle)
            }

            override fun onTaskLongClicked(anchor: View, task: Task) {

            }
        }

        val touchCallback =
            if (adapter is TaskListAdapter) TaskTouchCallback(adapter as TaskListAdapter)
            else TaskGroupTouchCallback(adapter as TaskGroupListAdapter)

        itemTouchHelper = ItemTouchHelper(touchCallback)
    }

    override fun onResume() {
        adapter.tasks = viewModel.getTasks().value!!
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        //(binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        adapter.refreshAll()
        super.onResume()
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

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        viewModel.getTasks().observe(viewLifecycleOwner, {
            Log.d("TM-", "TaskViewModel changed")
            adapter.tasks = it
            adapter.refreshAll()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_add) {
            findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
            return true
        }
        return false
    }
}