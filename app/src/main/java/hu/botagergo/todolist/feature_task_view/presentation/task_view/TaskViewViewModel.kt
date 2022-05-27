package hu.botagergo.todolist.feature_task_view.presentation.task_view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.botagergo.todolist.feature_task_view.data.filter.Filter
import hu.botagergo.todolist.feature_task_view.data.group.Grouper
import hu.botagergo.todolist.feature_task.data.Task
import hu.botagergo.todolist.feature_task_view.data.TaskView
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import hu.botagergo.todolist.feature_task_view.data.sorter.Sorter
import java.util.*

class TaskViewViewModel(val taskViewRepo: TaskViewRepository, val uuid: UUID?) : ViewModel() {

    val name: MutableLiveData<String?>
    private val description: MutableLiveData<String?>
    val filter: MutableLiveData<Filter<Task>?>
    val grouper: MutableLiveData<Grouper<Task>?>
    val sorter: MutableLiveData<Sorter<Task>?>

    init {
        val taskView: TaskView? = if (uuid != null) taskViewRepo.get(uuid) else null

        name = MutableLiveData(taskView?.name)
        description = MutableLiveData(taskView?.description)
        filter = MutableLiveData(taskView?.filter)
        grouper = MutableLiveData(taskView?.grouper?.clone())
        sorter = MutableLiveData(taskView?.sorter?.clone())
    }

    val taskView: TaskView by lazy {
        if (uuid != null) {
            TaskView(
                name.value!!,
                description.value,
                filter.value,
                grouper.value,
                sorter.value,
                uuid
            )
        } else {
            TaskView(
                name.value!!,
                description.value,
                filter.value,
                grouper.value,
                sorter.value
            )
        }
    }

}