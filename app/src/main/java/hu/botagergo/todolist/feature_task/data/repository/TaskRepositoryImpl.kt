package hu.botagergo.todolist.feature_task.data.repository

import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.feature_task.data.Task
import hu.botagergo.todolist.feature_task.data.TaskDao
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override fun getAll(): Flow<List<Task>> {
        return taskDao.getAll()
    }

    override suspend fun get(uid: Long): Task {
        return taskDao.get(uid)
    }

    override suspend fun insert(task: Task): Long {
        return taskDao.insert(task)
    }

    override suspend fun insertAll(tasks: Iterable<Task>) {
        tasks.forEach { taskDao.insert(it) }
    }

    override suspend fun update(task: Task) {
        taskDao.update(task)
    }

    override suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    override suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    override suspend fun addSampleData() {
        insertAll(
            listOf(
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
            )
        )
    }

}