package hu.botagergo.taskmanager.task_filter

import hu.botagergo.taskmanager.model.Task

abstract class TaskFilter {
    abstract fun include(task: Task): Boolean

    fun apply(tasks: ArrayList<Task>) {
        tasks.removeIf {
            !include(it)
        }
    }
}