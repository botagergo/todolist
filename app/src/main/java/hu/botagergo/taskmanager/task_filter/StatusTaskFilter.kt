package hu.botagergo.taskmanager.task_filter

import hu.botagergo.taskmanager.model.Task
import java.util.*

class StatusTaskFilter( var showStatus: Set<Task.Status> = EnumSet.allOf(Task.Status::class.java)) : TaskFilter() {

    override fun include(task: Task): Boolean {
        return showStatus.contains(task.status)
    }

}