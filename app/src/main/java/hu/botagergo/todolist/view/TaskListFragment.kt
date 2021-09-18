package hu.botagergo.todolist.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.*
import hu.botagergo.todolist.databinding.FragmentTaskListBinding
import hu.botagergo.todolist.group.PropertyGrouper
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import hu.botagergo.todolist.view_model.TaskListViewModel
import java.util.*

class TaskListFragment(private val taskListView: Configuration.TaskListView) : Fragment() {
    private lateinit var adapter: TaskArrayAdapter
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        val activity = requireActivity()
        adapter = TaskArrayAdapter(activity)

        adapter.tasks = viewModel.getTasks().value!!

        taskListView.filter.observe(requireActivity()) {
            adapter.filter = it
            adapter.refresh()
        }
        taskListView.grouper.observe(requireActivity()) {
            adapter.grouper = it
            adapter.refresh()
        }

        taskListView.sorter.observe(requireActivity()) {
            adapter.sorter = it
            adapter.refresh()
        }

        adapter.listener = object : TaskArrayAdapter.Listener {
            override fun onDoneClicked(task: Task, done: Boolean) {
                logd(this, "onDoneClicked")
                viewModel.updateTask(task.copy(done = !done))
            }

            override fun onTaskClicked(task: Task) {
                val bundle = bundleOf("uid" to task.uid)
                findNavController().navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle)
            }

            override fun onTaskLongClicked(anchor: View, task: Task) {

            }
        }
        adapter.refresh()
    }

    override fun onResume() {
        adapter.tasks = viewModel.getTasks().value!!
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.refresh()
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

        viewModel.getTasks().observe(viewLifecycleOwner, {
            Log.d("TM-", "TaskViewModel changed")
            adapter.tasks = it
            adapter.refresh()
        })

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sorter = adapter.sorter as TaskReorderableSorter
                val viewHolderFrom = viewHolder as TaskArrayAdapter.TaskViewHolder
                val viewHolderTo = target as TaskArrayAdapter.MyViewHolder

                if (viewHolderTo is TaskArrayAdapter.TaskViewHolder) {
                    val indFrom = sorter.taskUidList.indexOf(viewHolderFrom.task!!.uid)
                    val indTo = sorter.taskUidList.indexOf(viewHolderTo.task!!.uid)
                    Collections.swap(sorter.taskUidList, indFrom, indTo)
                    adapter.notifyItemMoved(viewHolderFrom.bindingAdapterPosition, viewHolderTo.bindingAdapterPosition)
                }

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

            override fun canDropOver(
                recyclerView: RecyclerView,
                current: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (current is TaskArrayAdapter.MyViewHolder &&
                    target is TaskArrayAdapter.MyViewHolder) {
                    if (target !is TaskArrayAdapter.GroupNameViewHolder
                        && current.group == target.group) {
                        return true
                    }
                }
                return false
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder !is TaskArrayAdapter.TaskViewHolder) {
                    return makeMovementFlags(0, 0)
                }

                val dragFlags = if (adapter.sorter is TaskReorderableSorter) {
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN
                } else 0

                return makeMovementFlags(dragFlags, 0)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
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