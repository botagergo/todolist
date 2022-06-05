package hu.botagergo.todolist.feature_task.presentation.task_list.adapter

import android.content.Context
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import hu.botagergo.todolist.feature_task_view.data.group.Grouper
import hu.botagergo.todolist.feature_task.data.model.TaskEntity

abstract class TaskListAdapter(
    val context: Context
) : GroupieAdapter() {

    protected val tasksData: MutableList<Grouper.Group<TaskEntity>> = mutableListOf()

    val rootSection = Section().apply { this@TaskListAdapter.add(this) }

    abstract var tasks: MutableList<Grouper.Group<TaskEntity>>?

    abstract fun refresh()
    abstract fun onItemSelected(taskItem: TaskItem)
    abstract fun moveItem(fromInd: Int, toInd: Int)

    protected fun updateTasks(
        section: Section,
        tasks: MutableList<TaskEntity>,
        newTasks: MutableList<TaskEntity>
    ) {
        var taskInd = 0
        var newTaskInd = 0

        while (taskInd < tasks.size && newTaskInd < newTasks.size) {
            if (tasks[taskInd] == newTasks[newTaskInd]) {
                // No change in the current task, leave it and continue
                taskInd++; newTaskInd++
            } else if (tasks[taskInd].uid == newTasks[newTaskInd].uid) {
                // The current task was updated, update the TaskItem
                tasks[taskInd] = newTasks[newTaskInd]
                val item = section.getItem(taskInd) as TaskItem
                item.task = newTasks[newTaskInd]
                item.notifyChanged()
            } else if (tasks[taskInd].uid != newTasks[newTaskInd].uid) {
                val groups = section.groups

                // If the current task in the recycler view doesn't exist in the new task list, just remove it
                if (newTasks.find { it.uid == tasks[taskInd].uid } == null) {
                    tasks.removeAt(taskInd)
                    groups.removeAt(taskInd)
                }
                // Otherwise find the current new task in the adapter and move it to the current position
                else {
                    val ind = tasks.indexOfLast {
                        it.uid == newTasks[newTaskInd].uid
                    }

                    if (ind != -1) {
                        if (ind <= taskInd) {
                            throw RuntimeException()
                        }
                        tasks.removeAt(ind)
                        groups.removeAt(ind)
                    }

                    tasks.add(newTaskInd, newTasks[newTaskInd])
                    groups.add(taskInd, TaskItem(this, newTasks[newTaskInd]))

                    taskInd++; newTaskInd++
                }
                section.update(groups)
            }
        }

        while (taskInd < tasks.size) {
            val groups = section.groups
            tasks.removeAt(taskInd)
            groups.removeAt(taskInd)
            section.update(groups)
        }

        while (newTaskInd < newTasks.size) {
            tasks.add(taskInd, newTasks[newTaskInd])
            val groups = section.groups
            groups.add(taskInd, TaskItem(this, newTasks[newTaskInd]))
            section.update(groups)
            taskInd++; newTaskInd++
        }
    }

    fun taskAt(bindingAdapterPosition: Int): TaskEntity? {
        return (getItem(bindingAdapterPosition) as? TaskItem)?.task
    }

    private var onItemDoneClicked: ((TaskEntity) -> Unit)? = null
    fun setOnItemDoneClickedListener(listener: (TaskEntity) -> Unit) {
        onItemDoneClicked = listener
    }

    private var onItemDeleteClicked: ((TaskEntity) -> Unit)? = null
    fun setOnItemDeleteClickedListener(listener: (TaskEntity) -> Unit) {
        onItemDeleteClicked = listener
    }

    private var onItemClicked: ((TaskEntity) -> Unit)? = null
    fun setOnItemClickedListener(listener: (TaskEntity) -> Unit) {
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
