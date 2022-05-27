package hu.botagergo.todolist.feature_task.presentation.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.botagergo.todolist.feature_task.data.Task
import hu.botagergo.todolist.core.util.EnumValue
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class TaskViewModel(private val taskRepo: TaskRepository, taskUid: Long) : ViewModel() {

    val title: MutableLiveData<String> = MutableLiveData("")
    val comments: MutableLiveData<String> = MutableLiveData("")
    val status: MutableLiveData<EnumValue?> = MutableLiveData(null)
    val context: MutableLiveData<EnumValue?> = MutableLiveData(null)
    val startDate: MutableLiveData<LocalDate?> = MutableLiveData()
    val startTime: MutableLiveData<LocalTime?> = MutableLiveData()
    val dueDate: MutableLiveData<LocalDate?> = MutableLiveData()
    val dueTime: MutableLiveData<LocalTime?> = MutableLiveData()
    val done: MutableLiveData<Boolean> = MutableLiveData(false)
    val uid: MutableLiveData<Long> = MutableLiveData()

    init {
        if (taskUid != 0L) {
            viewModelScope.launch(Dispatchers.Main) {
                val task = taskRepo.get(taskUid)
                title.value = task.title
                comments.value = task.comments
                status.value = task.status
                context.value = task.context
                startDate.value = task.startDate
                startTime.value = task.startTime
                dueDate.value = task.dueDate
                dueTime.value = task.dueTime
                done.value = task.done
                uid.value = task.uid
            }
        } else {
            uid.value = 0L
        }
    }

    val task: Task
        get() {
            return Task(
                title.value?:"", comments.value?:"", status.value, context.value,
                startDate.value, startTime.value, dueDate.value, dueTime.value,
                done.value?:false, uid.value!!
            )
        }

}