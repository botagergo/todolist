package hu.botagergo.todolist

import androidx.lifecycle.MutableLiveData
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.Sorter
import hu.botagergo.todolist.task_filter.ConjugateTaskFilter
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class Configuration : Serializable {
    class TaskListView(var name: String,
                       var filter: MutableLiveData<ConjugateTaskFilter?>,
                       var grouper: MutableLiveData<Grouper<Any, Task>?>,
                       var sorter: MutableLiveData<Sorter<Task>?>) : Serializable {

        class TaskListViewState : Serializable {
            var groupExpanded: MutableMap<String, Boolean> = LinkedHashMap()
            var groupOrder: MutableList<Any> = ArrayList()
            var taskOrder: List<Long> = ArrayList()
        }

        var taskListViewState: TaskListViewState = TaskListViewState()

        private fun writeObject(oos: ObjectOutputStream) {
            oos.writeObject(name)
            oos.writeObject(filter.value)
            oos.writeObject(grouper.value)
            oos.writeObject(sorter.value)
            oos.writeObject(taskListViewState)
        }

        private fun readObject(ois: ObjectInputStream) {
            name = ois.readObject() as String
            filter = MutableLiveData(ois.readObject() as ConjugateTaskFilter?)

            @Suppress("UNCHECKED_CAST")
            grouper = MutableLiveData(ois.readObject() as Grouper<Any, Task>?)

            @Suppress("UNCHECKED_CAST")
            sorter = MutableLiveData(ois.readObject() as Sorter<Task>?)

            taskListViewState = ois.readObject() as TaskListViewState
        }

    }

    var taskListViews: List<TaskListView> = ArrayList()

    class State: Serializable {
        var selectedTaskListViewName: String? = null
    }

    var state: State = State()

}