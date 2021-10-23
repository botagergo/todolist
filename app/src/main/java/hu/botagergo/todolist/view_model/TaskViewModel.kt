package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import hu.botagergo.todolist.model.AppDatabase
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskDao
import java.time.LocalDate
import java.time.LocalTime

class TaskViewModel(val app: Application, uid: Long) : ViewModel() {

    var title: String = ""
    var comments: String = ""
    var status: MutableLiveData<Task.Status?> = MutableLiveData(null)
    var context: MutableLiveData<Task.Context?> = MutableLiveData(null)
    var startDate: MutableLiveData<LocalDate?> = MutableLiveData()
    var startTime: MutableLiveData<LocalTime?> = MutableLiveData()
    var dueDate: MutableLiveData<LocalDate?> = MutableLiveData()
    var dueTime: MutableLiveData<LocalTime?> = MutableLiveData()
    private var done: Boolean = false
    var uid: Long = 0

    private var taskDao: TaskDao

    init {
        val db = Room.databaseBuilder(
            app, AppDatabase::class.java, "task"
        ).allowMainThreadQueries().build()
        taskDao = db.taskDao()

        if (uid != 0L) {
            val task = taskDao.get(uid)
            this.title = task.title
            this.comments = task.comments
            this.status.value = task.status
            this.context.value = task.context
            this.startDate.value = task.startDate
            this.startTime.value = task.startTime
            this.dueDate.value = task.dueDate
            this.dueTime.value = task.dueTime
            this.done = task.done
            this.uid = task.uid
        }
    }

    var statusIndex: Int
        get() {
            return status.value?.ordinal ?: -1
        }
        set(value) {
            status.value = Task.Status.values()[value]
        }

    var contextIndex: Int
        get() {
            return context.value?.ordinal ?: -1
        }
        set(value) {
            context.value = Task.Context.values()[value]
        }

    val task: Task
        get() {
            return Task(
                title, comments, status.value, context.value,
                startDate.value, startTime.value, dueDate.value, dueTime.value,
                done, uid
            )
        }

}