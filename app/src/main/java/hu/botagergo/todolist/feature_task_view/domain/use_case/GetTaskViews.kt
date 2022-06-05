package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.feature_task_view.data.model.TaskView
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository

class GetTaskViews(private val taskViewRepo: TaskViewRepository) {

    operator fun invoke(): List<TaskView> {
        return taskViewRepo.getAll()
    }

}