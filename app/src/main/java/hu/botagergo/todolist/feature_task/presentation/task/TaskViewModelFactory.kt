package hu.botagergo.todolist.feature_task.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository

class TaskViewModelFactory(private val taskRepo: TaskRepository, val uid: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TaskRepository::class.java, Long::class.java)
            .newInstance(taskRepo, uid)
    }

}