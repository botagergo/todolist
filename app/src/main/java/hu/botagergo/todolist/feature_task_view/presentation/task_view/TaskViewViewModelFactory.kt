package hu.botagergo.todolist.feature_task_view.presentation.task_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import java.util.*

class TaskViewViewModelFactory(val taskViewRepo: TaskViewRepository, private val uuid: UUID?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TaskViewRepository::class.java, UUID::class.java)
            .newInstance(taskViewRepo, uuid)
    }
}