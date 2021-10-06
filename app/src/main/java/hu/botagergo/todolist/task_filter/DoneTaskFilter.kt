package hu.botagergo.todolist.task_filter

import hu.botagergo.todolist.model.Task

class DoneTaskFilter(var showDone: Boolean = false, var showNotDone: Boolean = true) :
    TaskFilter() {

    override fun include(task: Task): Boolean {
        return task.done && showDone || !task.done && showNotDone
    }

}