package hu.botagergo.todolist.task_filter

import hu.botagergo.todolist.model.Task

class ConjugateTaskFilter(var doneFilter: DoneTaskFilter = DoneTaskFilter(),
                          var statusFilter: StatusTaskFilter = StatusTaskFilter(),
                          var contextFilter: ContextTaskFilter = ContextTaskFilter()) : TaskFilter() {

    override fun include(task: Task): Boolean {
        return doneFilter.include(task)
                && statusFilter.include(task)
                && contextFilter.include(task)
    }

}