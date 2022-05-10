package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.model.AppDatabase
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskDao
import java.time.LocalDate

class TaskListViewModel(application: Application) : AndroidViewModel(application) {
    private var _tasksLiveData: MutableLiveData<ArrayList<Task>>
    private var _tasks: ArrayList<Task>
    private var _taskDao: TaskDao
    private var app: ToDoListApplication = application as ToDoListApplication
    private val unit: () -> Unit = {}

    init {
        val db = Room.databaseBuilder(
            application, AppDatabase::class.java, "task"
        ).allowMainThreadQueries().build()

        _taskDao = db.taskDao()
        _tasks = ArrayList(_taskDao.getAll())
        _tasksLiveData = MutableLiveData(_tasks)
    }

    val tasks: LiveData<ArrayList<Task>>
        get() {
            return _tasksLiveData
        }

    fun addTask(task: Task) {
        val uid = _taskDao.insert(task.copy())
        _tasks.add(task.copy(uid = uid))
        _tasksLiveData.value = _tasks
        app.taskAddedEvent.signal(task)
    }

    fun deleteTask(task: Task) {
        _taskDao.delete(task)
        _tasks.remove(task)
        _tasksLiveData.value = _tasks
        app.taskRemovedEvent.signal(task)
    }

    fun deleteAll() {
        _taskDao.deleteAll()
        _tasks.clear()
        _tasksLiveData.value = _tasks

        app.taskDataSetChangedEvent.signal(unit())
    }

    fun updateTask(task: Task) {
        _taskDao.update(task)

        val index = _tasks.indexOfFirst { it.uid == task.uid }
        _tasks[index] = task

        _tasksLiveData.value = _tasks

        app.taskDataSetChangedEvent.signal(unit())
    }

    fun addSampleData() {
        addTask(
            Task(
                "NLP beadandót befejezni",
                "https://canvas.elte.hu/courses/20919/assignments/150825",
                Predefined.TaskStatusValues.nextAction,
                null, null, null, LocalDate.now().plusDays(1)
            )
        )
        addTask(
            Task(
                "Lordestint venni",
                "",
                Predefined.TaskStatusValues.waiting,
                Predefined.TaskContextValues.errands,
                null, null, null
            )
        )
        addTask(
            Task(
                "Rezsit átutalni",
                "",
                Predefined.TaskStatusValues.nextAction,
                null, null, null, null
            )
        )
        addTask(
            Task(
                "Hűtőszekrényt kiválasztani",
                "https://www.mediamarkt.hu/hu/product/_aeg-rke532f2dw-h%C5%B1t%C5%91szekr%C3%A9ny-155-cm-1319500.html\nhttps://www.mediamarkt.hu/hu/product/_zanussi-zran32fw-h%C5%B1t%C5%91szekr%C3%A9ny-155-cm-1326379.html\nhttps://www.mediamarkt.hu/hu/product/_zanussi-ztan28fw0-kombin%C3%A1lt-h%C5%B1t%C5%91szekr%C3%A9ny-160-cm-1333580.html\nhttps://www.mediamarkt.hu/hu/product/_lg-gtb382pzczd-fel%C3%BClfagyaszt%C3%B3s-kombin%C3%A1lt-h%C5%B1t%C5%91szekr%C3%A9ny-1336483.html#specifik_C3_A1ci_C3_B3",
                Predefined.TaskStatusValues.nextAction,
            )
        )
        addTask(
            Task(
                "Iskolalátogatási igazolás",
                "",
                Predefined.TaskStatusValues.nextAction,
                Predefined.TaskContextValues.errands,
                null, null, LocalDate.now().plusDays(4)
            )
        )
        addTask(
            Task(
                "Szakdolgozat témát keresni",
                "",
                Predefined.TaskStatusValues.planning,
                null, null, null, null
            )
        )
        addTask(
            Task(
                "Kérdezni szakmai gyakorlatról",
                "",
                Predefined.TaskStatusValues.nextAction,
                null, null, null, LocalDate.now().plusDays(7)
            )
        )
        addTask(
            Task(
                "Árvaellátási kérelem",
                "",
                Predefined.TaskStatusValues.nextAction,
                Predefined.TaskContextValues.errands,
                null, null, null
            )
        )

        app.taskDataSetChangedEvent.signal(unit())

    }
}