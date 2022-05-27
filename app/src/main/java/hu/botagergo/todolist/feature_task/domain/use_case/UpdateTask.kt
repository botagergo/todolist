package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.feature_task.data.Task
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository

class UpdateTask(private val taskRepo: TaskRepository) {

    suspend operator fun invoke(task: Task) {
        taskRepo.update(task)
    }

}