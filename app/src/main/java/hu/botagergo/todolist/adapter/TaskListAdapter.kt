package hu.botagergo.todolist.adapter

import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.TouchCallback
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.model.Task

open abstract class Adapter(
    val application: ToDoListApplication,
    var tasks: ArrayList<Task>, var taskListView: Configuration.TaskListView
) : GroupieAdapter() {

    lateinit var section: Section

    abstract fun refresh()
    abstract fun onItemSelected(taskItem: TaskItem)
    abstract fun getTouchCallback(): TouchCallback

    private var onItemDoneClicked: ((Task) -> Unit)? = null
    fun setOnItemDoneClickedListener(listener: (Task) -> Unit) {
        onItemDoneClicked = listener
    }

    private var onItemDeleteClicked: ((Task) -> Unit)? = null
    fun setOnItemDeleteClickedListener(listener: (Task) -> Unit) {
        onItemDeleteClicked = listener
    }

    private var onItemClicked: ((Task) -> Unit)? = null
    fun setOnItemClickedListener(listener: (Task) -> Unit) {
        onItemClicked = listener
    }

    fun onItemDoneClicked(taskItem: TaskItem) {
        onItemDoneClicked?.invoke(taskItem.task)
    }

    fun onItemDeleteClicked(taskItem: TaskItem) {
        onItemDeleteClicked?.invoke(taskItem.task)
    }

    fun onItemClicked(taskItem: TaskItem) {
        onItemClicked?.invoke(taskItem.task)
    }

}

fun createAdapter(application: ToDoListApplication, taskList: ArrayList<Task>, taskListView: Configuration.TaskListView): Adapter {
    return if (taskListView.grouper.value != null) {
        GroupedTaskListAdapter(application, taskList, taskListView)
    } else {
        SimpleTaskListAdapter(application, taskList, taskListView)
    }
}