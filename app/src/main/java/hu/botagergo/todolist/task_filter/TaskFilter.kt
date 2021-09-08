package hu.botagergo.todolist.task_filter

import hu.botagergo.todolist.model.Task

abstract class TaskFilter {
    abstract fun include(task: Task): Boolean

    fun apply(tasks: ArrayList<Task>) {
        tasks.removeIf {
            !include(it)
        }
    }
}