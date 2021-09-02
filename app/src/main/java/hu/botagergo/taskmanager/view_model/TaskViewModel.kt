package hu.botagergo.taskmanager.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.room.Room
import hu.botagergo.taskmanager.model.AppDatabase
import hu.botagergo.taskmanager.model.Task
import hu.botagergo.taskmanager.model.TaskDao

class TaskViewModel(application: Application, uid: Long) : ViewModel() {

    var title: String = ""
    var comments: String = ""
    var status: Task.Status = Task.Status.None
    var context: Task.Context = Task.Context.None
    var done: Boolean =  false
    var uid: Long = 0

    private var taskDao: TaskDao

    init {
        val db = Room.databaseBuilder(
            application, AppDatabase::class.java, "task"
        ).allowMainThreadQueries().build()
        taskDao = db.taskDao()

        if (uid != 0L) {
            val task = taskDao.get(uid)
            this.title = task.title
            this.comments = task.comments
            this.status = task.status
            this.context = task.context
            this.done = task.done
            this.uid = task.uid
        }
    }

    var statusIndex: Int
        get() {
            return status.ordinal
        }
        set(value) {
            status = Task.Status.values()[value]
        }

    var contextIndex: Int
        get() {
            return context.ordinal
        }
        set(value) {
            context = Task.Context.values()[value]
        }

    val task: Task
        get() {
            return Task(title, comments, status, context, done, uid)
        }
}