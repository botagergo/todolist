package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task_view.data.model.TaskView
import hu.botagergo.todolist.feature_task_view.data.group.Grouper
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class GetTaskGroups(private val taskRepo: TaskRepository) {

    operator fun invoke(taskView: TaskView): Flow<List<Grouper.Group<TaskEntity>>> {
        return taskRepo.getAll().map {
            ArrayList(it)
        }.onEach {
            taskView.filter?.apply(it)
            taskView.sorter?.sort(it)
        }.map {
            taskView.grouper?.group(it) ?: listOf(Grouper.Group("", it))
        }
    }

}