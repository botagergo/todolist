package hu.botagergo.todolist.feature_task_view.domain

import hu.botagergo.todolist.feature_task.data.model.Task
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterEntity
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterInfo
import hu.botagergo.todolist.feature_task_view.domain.model.filter.Filter

interface TaskFilterRepository {

    suspend fun get(id: Long): Filter<Task>
    suspend fun getInfo(id: Long): TaskFilterInfo
    suspend fun insert(filter: TaskFilterEntity)

}