package hu.botagergo.todolist.task_filter

import hu.botagergo.todolist.model.Task
import java.io.Serializable

abstract class TaskFilter : Serializable {
    abstract fun include(task: Task): Boolean

    fun apply(tasks: ArrayList<Task>) {
        tasks.removeIf {
            !include(it)
        }
    }
}