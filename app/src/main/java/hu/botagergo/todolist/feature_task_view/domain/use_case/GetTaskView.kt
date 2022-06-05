package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.feature_task_view.data.model.TaskView
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import java.util.*

class GetTaskView(private val taskViewRepo: TaskViewRepository) {

    operator fun invoke(uuid: UUID): TaskView {
        return taskViewRepo.get(uuid)
    }

}