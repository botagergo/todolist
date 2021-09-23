package hu.botagergo.todolist.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.R
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import hu.botagergo.todolist.view_model.TaskListViewModel
import java.util.*
import java.util.stream.Collectors

class TaskGroupListAdapter(
    activity: Activity,
    tasks: List<Task>,
    viewModel: TaskListViewModel,
    taskListView: Configuration.TaskListView
) : Adapter(activity, tasks, viewModel, taskListView) {

    lateinit var taskGroups: List<Pair<Any, List<Task>>>
    lateinit var adapters: List<TaskListAdapter>

    init {
        refreshAll()
    }

    override fun refreshAll() {
        taskGroups = taskListView.grouper.value!!.group(tasks).toList()
        adapters = taskGroups.stream().map {
            createAdapter(it)
        }.collect(Collectors.toList())
        notifyDataSetChanged()
    }

    inner class TaskGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button_groupHeader = view.findViewById<Button>(R.id.button_groupHeader)
        val recyclerView_taskList = view.findViewById<RecyclerView>(R.id.recyclerView_taskList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskGroupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskGroupViewHolder(inflater.inflate(R.layout.item_task_group, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val taskGroupViewHolder = viewHolder as TaskGroupViewHolder
        taskGroupViewHolder.button_groupHeader.text = taskGroups[position].first.toString()

        taskGroupViewHolder.recyclerView_taskList.adapter = adapters[position]
        taskGroupViewHolder.recyclerView_taskList.layoutManager = LinearLayoutManager(activity)

        val taskTouchCallback = TaskTouchCallback(adapters[viewHolder.bindingAdapterPosition])
        val itemTouchHelper = ItemTouchHelper(taskTouchCallback)
        itemTouchHelper.attachToRecyclerView(taskGroupViewHolder.recyclerView_taskList)
    }

    override fun getItemCount(): Int {
        return taskGroups.size
    }

    private fun createAdapter(group: Pair<Any, List<Task>>): TaskListAdapter {
        val adapter = TaskListAdapter(activity, group.second, viewModel, taskListView, this) {
            val ind = taskGroups.indexOfFirst {
                it.first == group.first
            }

            if (ind != -1) {
                logd(this, "$ind")
                notifyItemChanged(ind)
            }
        }

        adapter.listener = object : Listener {
            override fun onDoneOrUndoneClicked(task: Task, done: Boolean) {
                listener?.onDoneOrUndoneClicked(task, done)
            }

            override fun onDeleteClicked(task: Task) {
                listener?.onDeleteClicked(task)
            }

            override fun onTaskClicked(task: Task) {
                listener?.onTaskClicked(task)
            }

            override fun onTaskLongClicked(anchor: View, task: Task) {
                listener?.onTaskLongClicked(anchor, task)
            }
        }

        return adapter
    }
}

