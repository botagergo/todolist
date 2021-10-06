package hu.botagergo.todolist.task_filter

import hu.botagergo.todolist.model.Task
import java.util.*

class StatusTaskFilter(private var showStatus: Set<Task.Status> = EnumSet.allOf(Task.Status::class.java)) :
    TaskFilter() {

    override fun include(task: Task): Boolean {
        return showStatus.contains(task.status)
    }

}