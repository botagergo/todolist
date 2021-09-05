package hu.botagergo.taskmanager.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SwitchCompat
import androidx.core.util.forEach
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import hu.botagergo.taskmanager.*
import hu.botagergo.taskmanager.databinding.FragmentTaskListBinding
import hu.botagergo.taskmanager.group.PropertyGrouper
import hu.botagergo.taskmanager.log.logd
import hu.botagergo.taskmanager.model.Task
import hu.botagergo.taskmanager.task_filter.ConjugateTaskFilter
import hu.botagergo.taskmanager.view_model.TaskListViewModel
import java.util.*

class TaskListFragment : Fragment() {
    private lateinit var adapter: TaskArrayAdapter
    private val viewModel: TaskListViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskListBinding
    private var listener: Listener? = null

    interface Listener {
        fun onAddTask()
        fun onEditTask(task: Task)
        fun onDoneTask(task: Task, done: Boolean)
        fun onDeleteTask(task: Task)
        fun onCreate(taskListFragment: TaskListFragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        super.onCreate(savedInstanceState)

        val activity = requireActivity()
        adapter = TaskArrayAdapter(activity)

        adapter.tasks = viewModel.getTasks().value!!
        adapter.filter = ConjugateTaskFilter()
        adapter.grouper = PropertyGrouper(Task::status) { p1, p2 ->
            p1.toString().compareTo(p2.toString())
        }
        adapter.listener = object : TaskArrayAdapter.Listener {
            override fun onDoneClicked(task: Task, done: Boolean) {
                logd(this, "onDoneClicked")
                listener?.onDoneTask(task, done)
            }

            override fun onTaskClicked(task: Task) {
                listener?.onEditTask(task)
            }

            override fun onTaskLongClicked(anchor: View, task: Task) {
                val popupMenu = PopupMenu(context!!, anchor)
                popupMenu.menuInflater.inflate(R.menu.menu_task_list_popup, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    if (it.itemId == R.id.menu_item_delete) {
                        listener?.onDeleteTask(task)
                        true
                    } else {
                        false
                    }
                }
                popupMenu.show()
            }
        }
        adapter.refresh()

        listener?.onCreate(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener
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
            listener?.onAddTask()
        }
    }

    fun onNavViewTaskStatusClicked(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle("Task Status")

        builder.setPositiveButton("OK") { dialogInterface, i ->
            val dialog = dialogInterface as AlertDialog
            val positions = dialog.listView.checkedItemPositions
            val set: EnumSet<Task.Status> = EnumSet.noneOf(Task.Status::class.java)
            positions.forEach { pos, b ->
                if (b) {
                    logd(this, "true")
                    set.add(Task.Status.values()[pos])
                }
            }
            logd(this, set)
            adapter.filter!!.statusFilter.showStatus = set
            adapter.refresh()
            dialogInterface.dismiss()
        }

        val array = BooleanArray(Task.Status.values().size)
        val showStatus = adapter.filter!!.statusFilter.showStatus
        for (i in Task.Status.values().indices) {
            array[i] = showStatus.contains(Task.Status.values()[i])
        }

        builder.setMultiChoiceItems(
            Task.Status.values().map { it.value }.toTypedArray(),
            array, null)

        val dialog = builder.create()
        dialog.show()
    }

    fun onNavViewTaskContextClicked(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle("Task Context")

        builder.setPositiveButton("OK") { dialogInterface, i ->
            val dialog = dialogInterface as AlertDialog
            val positions = dialog.listView.checkedItemPositions
            val set: EnumSet<Task.Context> = EnumSet.noneOf(Task.Context::class.java)
            positions.forEach { pos, b ->
                if (b) {
                    logd(this, "true")
                    set.add(Task.Context.values()[pos])
                }
            }
            logd(this, set)
            adapter.filter!!.contextFilter.showContext = set
            adapter.refresh()
            dialogInterface.dismiss()
        }

        val array = BooleanArray(Task.Context.values().size)
        val showContext = adapter.filter!!.contextFilter.showContext
        for (i in Task.Context.values().indices) {
            array[i] = showContext.contains(Task.Context.values()[i])
        }

        builder.setMultiChoiceItems(
            Task.Context.values().map { it.value }.toTypedArray(),
            array, null)

        val dialog = builder.create()
        dialog.show()
    }

    fun onNavViewGroupByClicked(context: Context, view: View) {
        val popupMenu = PopupMenu(this.requireContext(), view)
        popupMenu.menu.add(Menu.NONE, 0, Menu.NONE, "Status")
        popupMenu.menu.add(Menu.NONE, 1, Menu.NONE, "Context")
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == 0) {
                adapter.grouper = PropertyGrouper(Task::status) {p1, p2 ->
                    p1.toString().compareTo(p2.toString())
                }
                adapter.refresh()
            } else if (it.itemId == 1) {
                adapter.grouper = PropertyGrouper(Task::context) {p1, p2 ->
                    p1.toString().compareTo(p2.toString())
                }
                adapter.refresh()
            } else {
                false
            }
            true
        }

        popupMenu.show()
    }
}