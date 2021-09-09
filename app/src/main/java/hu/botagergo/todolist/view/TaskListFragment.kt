package hu.botagergo.todolist.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import hu.botagergo.todolist.*
import hu.botagergo.todolist.databinding.FragmentTaskListBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.view_model.TaskListViewModel

class TaskListFragment : Fragment() {
    private lateinit var adapter: TaskArrayAdapter
    private val viewModel: TaskListViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        super.onCreate(savedInstanceState)

        val activity = requireActivity()
        adapter = TaskArrayAdapter(activity)

        adapter.tasks = viewModel.getTasks().value!!

        val app = requireActivity().application as ToDoListApplication
        app.configuration.taskFilter.observe(requireActivity()) {
            adapter.filter = it
            adapter.refresh()
        }
        app.configuration.taskGrouper.observe(requireActivity()) {
            adapter.grouper = it
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
                val popupMenu = PopupMenu(context!!, anchor)
                popupMenu.menuInflater.inflate(R.menu.menu_task_list_popup, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    if (it.itemId == R.id.menu_item_delete) {
                        viewModel.deleteTask(task)
                        true
                    } else {
                        false
                    }
                }
                popupMenu.show()
            }
        }
        adapter.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logd(this, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
        val actionView = navView.menu.findItem(R.id.menu_item_show_done).actionView
        val switch = actionView.findViewById<SwitchCompat>(R.id.switch_showDone)
        switch.setOnCheckedChangeListener { _, b ->
            adapter.filter!!.doneFilter.showDone = b
            adapter.refresh()
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)

        viewModel.getTasks().observe(viewLifecycleOwner, {
            Log.d("TM-", "TaskViewModel changed")
            adapter.tasks = it
            adapter.refresh()
        })

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }
    }
}