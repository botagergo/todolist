package hu.botagergo.todolist

import androidx.lifecycle.MutableLiveData
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.task_filter.ConjugateTaskFilter

class Configuration {
    var taskFilter: MutableLiveData<ConjugateTaskFilter?> = MutableLiveData(null)
    var taskGrouper: MutableLiveData<Grouper<Any, Task>?> = MutableLiveData(null)
}