package hu.botagergo.todolist.adapter

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.view_model.TaskListViewModel

abstract class Adapter(
    val activity: Activity,
    var tasks: List<Task>,
    var viewModel: TaskListViewModel,
    var taskListView: Configuration.TaskListView)
        : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract fun refreshAll()

    interface Listener {
        fun onDoneOrUndoneClicked(task: Task, done: Boolean)
        fun onDeleteClicked(task: Task)
        fun onTaskClicked(task: Task)
        fun onTaskLongClicked(anchor: View, task: Task)
    }
    var listener: Listener? = null

}