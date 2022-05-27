package hu.botagergo.todolist.feature_task_view.domain

import hu.botagergo.todolist.feature_task_view.data.TaskView
import java.util.*

interface TaskViewRepository {
    fun get(uuid: UUID): TaskView
    fun getAll(): List<TaskView>
    fun insert(taskView: TaskView)
    fun insertAll(taskViews: Iterable<TaskView>)
}