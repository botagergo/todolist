package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.exception.TaskNotFoundException
import hu.botagergo.todolist.model.TaskDatabase
import hu.botagergo.todolist.model.Task
import java.time.LocalDate

class TaskListViewModel(app: Application) : AndroidViewModel(app) {

    private val app = app as ToDoListApplication
    private val db = Room.databaseBuilder(
        app, TaskDatabase::class.java, "task"
    ).allowMainThreadQueries().build()
    private val taskDao = db.taskDao()

    val tasks: ArrayList<Task> = ArrayList(taskDao.getAll())

    fun addTask(task: Task) {
        val uid = taskDao.insert(task)
        tasks.add(task.copy(uid = uid))
        app.taskAddedEvent.signal(task)
    }

    fun deleteTask(task: Task) {
        taskDao.delete(task)
        tasks.remove(task)
        app.taskRemovedEvent.signal(task)
    }

    fun deleteAll() {
        taskDao.deleteAll()
        tasks.clear()
        app.taskDataSetChangedEvent.signal(Unit)
    }

    fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.uid == task.uid }
        if (index == -1) {
            throw TaskNotFoundException(task.uid)
        }

        tasks[index] = task
        taskDao.update(task)

        app.taskDataSetChangedEvent.signal(Unit)
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

        app.taskDataSetChangedEvent.signal(Unit)

    }
}