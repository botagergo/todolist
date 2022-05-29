package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.feature_task.data.Task
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository

class GetTask(private val taskRepo: TaskRepository) {

    suspend operator fun invoke(uid: Long): Task {
        return taskRepo.get(uid)
    }

}