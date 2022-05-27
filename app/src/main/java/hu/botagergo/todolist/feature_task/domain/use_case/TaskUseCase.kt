package hu.botagergo.todolist.feature_task.domain.use_case

import javax.inject.Inject

data class TaskUseCase @Inject constructor(
    val getTaskGroups: GetTaskGroups,
    val updateTask: UpdateTask,
    val deleteTask: DeleteTask
)
