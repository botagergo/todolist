package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.exception.TaskNotFoundException
import hu.botagergo.todolist.model.TaskDatabase
import hu.botagergo.todolist.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskListViewModel(app: Application) : AndroidViewModel(app) {

    private val app = app as ToDoListApplication
    private val taskDao = this.app.taskDao
    val tasks: ArrayList<Task> = ArrayList()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            tasks.addAll(taskDao.getAll())
            this@TaskListViewModel.app.taskDataSetChangedEvent.signal(Unit)
        }
    }

    fun addTask(task: Task) = viewModelScope.launch {
        val uid = taskDao.insert(task)
        tasks.add(task.copy(uid = uid))
        app.taskAddedEvent.signal(task)
        app.taskDataSetChangedEvent.signal(Unit)
    }

    fun addTasks(tasks: Iterable<Task>) = viewModelScope.launch {
        for (task in tasks) {
            val uid = taskDao.insert(task)
            this@TaskListViewModel.tasks.add(task.copy(uid = uid))
        }
        app.taskDataSetChangedEvent.signal(Unit)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        tasks.remove(task)
        app.taskRemovedEvent.signal(task)
        app.taskDataSetChangedEvent.signal(Unit)
    }

    fun deleteAll() = viewModelScope.launch {
        taskDao.deleteAll()
        tasks.clear()
        app.taskDataSetChangedEvent.signal(Unit)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        val index = tasks.indexOfFirst { it.uid == task.uid }
        if (index == -1) {
            throw TaskNotFoundException(task.uid)
        }

        tasks[index] = task
        taskDao.update(task)

        app.taskDataSetChangedEvent.signal(Unit)
    }

    fun addSampleData() {
        addTasks(listOf(
            Task(
                "NLP beadandót befejezni",
                "https://canvas.elte.hu/courses/20919/assignments/150825",
                Predefined.TaskStatusValues.nextAction,
                null, null, null, LocalDate.now().plusDays(1)
            ),
            Task(
                "Lordestint venni",
                "",
                Predefined.TaskStatusValues.waiting,
                Predefined.TaskContextValues.errands,
                null, null, null
            ),
            Task(
                "Rezsit átutalni",
                "",
                Predefined.TaskStatusValues.nextAction,
                null, null, null, null
            ),
            Task(
                "Hűtőszekrényt kiválasztani",
                "https://www.mediamarkt.hu/hu/product/_aeg-rke532f2dw-h%C5%B1t%C5%91szekr%C3%A9ny-155-cm-1319500.html\nhttps://www.mediamarkt.hu/hu/product/_zanussi-zran32fw-h%C5%B1t%C5%91szekr%C3%A9ny-155-cm-1326379.html\nhttps://www.mediamarkt.hu/hu/product/_zanussi-ztan28fw0-kombin%C3%A1lt-h%C5%B1t%C5%91szekr%C3%A9ny-160-cm-1333580.html\nhttps://www.mediamarkt.hu/hu/product/_lg-gtb382pzczd-fel%C3%BClfagyaszt%C3%B3s-kombin%C3%A1lt-h%C5%B1t%C5%91szekr%C3%A9ny-1336483.html#specifik_C3_A1ci_C3_B3",
                Predefined.TaskStatusValues.nextAction,
            ),
            Task(
                "Iskolalátogatási igazolás",
                "",
                Predefined.TaskStatusValues.nextAction,
                Predefined.TaskContextValues.errands,
                null, null, LocalDate.now().plusDays(4)
            ),
            Task(
                "Szakdolgozat témát keresni",
                "",
                Predefined.TaskStatusValues.planning,
                null, null, null, null
            ),
            Task(
                "Kérdezni szakmai gyakorlatról",
                "",
                Predefined.TaskStatusValues.nextAction,
                null, null, null, LocalDate.now().plusDays(7)
            ),
            Task(
                "Árvaellátási kérelem",
                "",
                Predefined.TaskStatusValues.nextAction,
                Predefined.TaskContextValues.errands,
                null, null, null
            )
        ))
    }
}