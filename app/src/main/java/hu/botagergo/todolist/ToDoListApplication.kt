package hu.botagergo.todolist

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.task_filter.ConjugateTaskFilter

class ToDoListApplication : Application() {

    val configuration: Configuration = Configuration()

    val taskAddedEvent: Event<Task> = Event()
    val taskChangedEvent: Event<Task> = Event()
    val taskRemovedEvent: Event<Task> = Event()
    val taskDataSetChangedEvent: Event<Unit> = Event()

}