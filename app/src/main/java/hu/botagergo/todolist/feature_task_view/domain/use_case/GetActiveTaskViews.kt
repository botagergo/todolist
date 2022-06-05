package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.config
import hu.botagergo.todolist.feature_task_view.data.model.TaskView
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import java.lang.Exception

class GetActiveTaskViews(private val taskViewRepo: TaskViewRepository) {

    operator fun invoke(): List<TaskView> {
        return config.activeTaskViews.mapNotNull { uuid ->
            try {
                taskViewRepo.get(uuid)
            } catch (e: Exception) {
                null
            }
        }
    }

}