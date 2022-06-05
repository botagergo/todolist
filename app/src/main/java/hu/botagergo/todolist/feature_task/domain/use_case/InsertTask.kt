package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository

class InsertTask(private val taskRepo: TaskRepository) {

    suspend operator fun invoke(task: TaskEntity) {
        taskRepo.insert(task)
    }

}