package hu.botagergo.todolist.feature_task.domain.use_case

import javax.inject.Inject

data class TaskViewUseCase @Inject constructor(
    val getTaskViews: GetTaskViews,
    val getActiveTaskViews: GetActiveTaskViews,
    val getTaskView: GetTaskView,
    val addActiveTaskView: AddActiveTaskView,
    val deleteActiveTaskView: DeleteActiveTaskView
)
