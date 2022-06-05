package hu.botagergo.todolist.feature_task.data.repository

import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.data.TodoListDao
import hu.botagergo.todolist.feature_task.data.model.Task
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository
import hu.botagergo.todolist.feature_task.mapper.toTask
import hu.botagergo.todolist.feature_task.mapper.toTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TaskRepositoryImpl(private val todoListDao: TodoListDao) : TaskRepository {

    override fun getAll(): Flow<List<Task>> {
        return todoListDao.getTasks()
            .map { it ->
                it.map { it.toTask() }
            }
    }

    override suspend fun get(uid: Long): Task {
        return todoListDao.getTask(uid).toTask()
    }

    override suspend fun insert(task: Task): Long {
        return todoListDao.insertTask(task.toTaskEntity())
    }

    override suspend fun insertAll(tasks: Iterable<Task>) {
        tasks.forEach { todoListDao.insertTask(it.toTaskEntity()) }
    }

    override suspend fun update(task: Task) {
        todoListDao.updateTask(task.toTaskEntity())
    }

    override suspend fun delete(task: Task) {
        todoListDao.deleteTask(task.toTaskEntity())
    }

    override suspend fun deleteAll() {
        todoListDao.deleteTasks()
    }

    override suspend fun addSampleData() {
        insertAll(
            listOf(
                Task(
                    "NLP beadandót befejezni",
                    "https://canvas.elte.hu/courses/20919/assignments/150825",
                    Predefined.TaskStatusValues.nextAction, null,
                    null, null, LocalDate.now().plusDays(1), null,
                    false
                ),
                Task(
                    "Lordestint venni",
                    "",
                    Predefined.TaskStatusValues.waiting, Predefined.TaskContextValues.errands,
                    null, null, null, null,
                    false
                ),
                Task(
                    "Rezsit átutalni",
                    "",
                    Predefined.TaskStatusValues.nextAction,  null,
                    null, null, null, null,
                    false
                ),
                Task(
                    "Hűtőszekrényt kiválasztani",
                    "https://www.mediamarkt.hu/hu/product/_aeg-rke532f2dw-h%C5%B1t%C5%91szekr%C3%A9ny-155-cm-1319500.html\nhttps://www.mediamarkt.hu/hu/product/_zanussi-zran32fw-h%C5%B1t%C5%91szekr%C3%A9ny-155-cm-1326379.html\nhttps://www.mediamarkt.hu/hu/product/_zanussi-ztan28fw0-kombin%C3%A1lt-h%C5%B1t%C5%91szekr%C3%A9ny-160-cm-1333580.html\nhttps://www.mediamarkt.hu/hu/product/_lg-gtb382pzczd-fel%C3%BClfagyaszt%C3%B3s-kombin%C3%A1lt-h%C5%B1t%C5%91szekr%C3%A9ny-1336483.html#specifik_C3_A1ci_C3_B3",
                    Predefined.TaskStatusValues.nextAction, null,
                    null, null, null, null,
                    false
                ),
                Task(
                    "Iskolalátogatási igazolás",
                    "",
                    Predefined.TaskStatusValues.nextAction,
                    Predefined.TaskContextValues.errands,
                    null, null, LocalDate.now().plusDays(4), null,
                    false
                ),
                Task(
                    "Szakdolgozat témát keresni",
                    "",
                    Predefined.TaskStatusValues.planning, null,
                    null, null, null, null,
                    false
                ),
                Task(
                    "Kérdezni szakmai gyakorlatról",
                    "",
                    Predefined.TaskStatusValues.nextAction, null,
                    null, null, LocalDate.now().plusDays(7), null,
                    false
                ),
                Task(
                    "Árvaellátási kérelem",
                    "",
                    Predefined.TaskStatusValues.nextAction,
                    Predefined.TaskContextValues.errands,
                    null, null, null, null,
                    false
                )
            )
        )
    }

}