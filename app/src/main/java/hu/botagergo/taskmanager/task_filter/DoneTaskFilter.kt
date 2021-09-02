package hu.botagergo.taskmanager.task_filter

import hu.botagergo.taskmanager.model.Task

class DoneTaskFilter(var showDone: Boolean = true, var showNotDone: Boolean = true) : TaskFilter() {

    override fun include(task: Task): Boolean {
        return task.done && showDone || !task.done && showNotDone
    }

}