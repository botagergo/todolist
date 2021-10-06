package hu.botagergo.todolist.task_filter

import hu.botagergo.todolist.model.Task

class ConjugateTaskFilter(private vararg val filters: TaskFilter) : TaskFilter() {
    override fun include(task: Task): Boolean {
        return filters.all {
            it.include(task)
        }
    }
}