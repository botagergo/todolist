package hu.botagergo.todolist

import android.app.Application
import hu.botagergo.todolist.model.Task

class ToDoListApplication : Application() {

    val taskAddedEvent: Event<Task> = Event()
    val taskChangedEvent: Event<Task> = Event()
    val taskRemovedEvent: Event<Task> = Event()
    val taskDataSetChangedEvent: Event<Unit> = Event()

    override fun onCreate() {
        config = Configuration.load(this)
        super.onCreate()
    }

}