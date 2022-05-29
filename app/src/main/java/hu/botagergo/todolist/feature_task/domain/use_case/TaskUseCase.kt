package hu.botagergo.todolist.feature_task.domain.use_case

import javax.inject.Inject

data class TaskUseCase @Inject constructor(
    val getTask: GetTask,
    val getTaskGroups: GetTaskGroups,
    val insertTask: InsertTask,
    val updateTask: UpdateTask,
    val deleteTask: DeleteTask
)
