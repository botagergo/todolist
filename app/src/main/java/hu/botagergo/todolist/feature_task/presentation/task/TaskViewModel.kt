package hu.botagergo.todolist.feature_task.presentation.task

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.botagergo.todolist.EXTRA_IS_EDIT
import hu.botagergo.todolist.EXTRA_UID
import hu.botagergo.todolist.core.util.EnumValue
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task.domain.use_case.TaskUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase,
    savedStateHandle: SavedStateHandle
    ) : ViewModel() {

    val title: MutableState<String> = mutableStateOf("")
    val comments: MutableState<String> = mutableStateOf("")
    val status: MutableState<EnumValue?> = mutableStateOf(null)
    val context: MutableState<EnumValue?> = mutableStateOf(null)
    val startDate: MutableState<LocalDate?> = mutableStateOf(null)
    val startTime: MutableState<LocalTime?> = mutableStateOf(null)
    val dueDate: MutableState<LocalDate?> = mutableStateOf(null)
    val dueTime: MutableState<LocalTime?> = mutableStateOf(null)

    private var done: Boolean = false
    private var uid: Long = 0

    private var isEdit = savedStateHandle.get<Boolean>(EXTRA_IS_EDIT) ?: throw IllegalArgumentException()

    init {
        savedStateHandle.get<Long>(EXTRA_UID)?.let { uid ->
            this.uid = uid

            viewModelScope.launch(Dispatchers.Main) {
                val task = taskUseCase.getTask(uid)
                title.value = task.title
                comments.value = task.comments
                status.value = task.status
                context.value = task.context
                startDate.value = task.startDate
                startTime.value = task.startTime
                dueDate.value = task.dueDate
                dueTime.value = task.dueTime
                done = task.done
            }
        }
    }

    fun save() {
        if (isEdit) {
            viewModelScope.launch {
                taskUseCase.updateTask(task)
            }
        } else {
            viewModelScope.launch {
                taskUseCase.insertTask(task)
            }
        }
    }

    private val task: TaskEntity
        get() {
            return TaskEntity(
                title.value, comments.value, status.value, context.value,
                startDate.value, startTime.value, dueDate.value, dueTime.value,
                done, uid
            )
        }

}