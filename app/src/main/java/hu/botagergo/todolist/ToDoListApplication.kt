package hu.botagergo.todolist

import android.app.Application
import androidx.room.Room
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskDao
import hu.botagergo.todolist.model.TaskDatabase

class ToDoListApplication : Application() {

    private val taskDb: TaskDatabase by lazy {
        Room.databaseBuilder(
            this, TaskDatabase::class.java, "task"
        ).fallbackToDestructiveMigration().build()
    }

    lateinit var taskDao: TaskDao

    val taskAddedEvent: Event<Task> = Event()
    val taskChangedEvent: Event<Task> = Event()
    val taskRemovedEvent: Event<Task> = Event()
    val taskDataSetChangedEvent: Event<Unit> = Event()

    override fun onCreate() {
        taskDao = taskDb.taskDao()
        Configuration.load(this)
        super.onCreate()
    }

}