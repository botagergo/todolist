package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.botagergo.todolist.config
import hu.botagergo.todolist.filter.Filter
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskView
import hu.botagergo.todolist.sorter.Sorter
import java.util.*

class TaskViewViewModel(val app: Application, val uuid: UUID?) : ViewModel() {

    val name: MutableLiveData<String?>
    private val description: MutableLiveData<String?>
    val filter: MutableLiveData<Filter<Task>?>
    val grouper: MutableLiveData<Grouper<Task>?>
    val sorter: MutableLiveData<Sorter<Task>?>

    init {
        val taskView: TaskView? = if (uuid != null) config.taskViews[uuid]!! else null

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