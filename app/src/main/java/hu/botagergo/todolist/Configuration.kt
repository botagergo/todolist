package hu.botagergo.todolist

import androidx.lifecycle.MutableLiveData
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.task_filter.ConjugateTaskFilter

class Configuration {
    class TaskListView(var name: String,
                       var filter: MutableLiveData<ConjugateTaskFilter?>,
                       var grouper: MutableLiveData<Grouper<Any, Task>?>) {}
    lateinit var taskListViews: List<TaskListView>
}