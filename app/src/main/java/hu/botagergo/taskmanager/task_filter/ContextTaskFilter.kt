package hu.botagergo.taskmanager.task_filter

import hu.botagergo.taskmanager.model.Task
import java.util.*

class ContextTaskFilter( var showContext: Set<Task.Context> = EnumSet.allOf(Task.Context::class.java)) : TaskFilter() {

    override fun include(task: Task): Boolean {
        return showContext.contains(task.context)
    }

}