package hu.botagergo.todolist.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import hu.botagergo.todolist.TaskView
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.model.Task

abstract class Adapter(
    val application: ToDoListApplication,
    var tasks: ArrayList<Task>, var taskView: TaskView
) : GroupieAdapter() {

    lateinit var section: Section

    abstract fun refresh()
    abstract fun onItemSelected(taskItem: TaskItem)
    abstract fun getItemTouchHelper(): ItemTouchHelper?

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

fun createAdapter(
    application: ToDoListApplication,
    taskList: ArrayList<Task>,
    taskListView: TaskView
): Adapter {
    return if (taskListView.grouper != null) {
        GroupedTaskListAdapter(application, taskList, taskListView)
    } else {
        SimpleTaskListAdapter(application, taskList, taskListView)
    }
}