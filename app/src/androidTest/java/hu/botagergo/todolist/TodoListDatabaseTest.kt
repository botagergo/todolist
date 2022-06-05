package hu.botagergo.todolist

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.data.TodoListDao
import hu.botagergo.todolist.data.TodoListDatabase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class TodoListDatabaseTest {

    private lateinit var context: Context
    private lateinit var db: TodoListDatabase
    private lateinit var todoListDao: TodoListDao

    @Before
    fun initDb() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, TodoListDatabase::class.java).build()
        todoListDao = db.todoListDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGet() = runBlocking {
        val task1 = insert(exampleTask)
        val task2 = get(task1.uid)
        assertEquals(task1, task2)
    }

    @Test
    fun testInsert() = runBlocking {
        insert("test1")
        insert("test2")
        assertEquals(2, todoListDao.getTasks().size)
        insert("test3")
        assertEquals(3, todoListDao.getTasks().size)
    }

    @Test
    fun testInsertWithSpecificUid(): Unit = runBlocking {
        insert("test1", 123)
        get(123)
    }

    @Test
    fun testInsertExisting() = runBlocking {
        val task1 = insert("test1")
        try {
            insert("test2", task1.uid)
            assert(false)
        } catch (e: SQLiteConstraintException) {
            assert(true)
        }
    }

    @Test
    fun testDelete() = runBlocking {
        val task1 = insert("test1")
        val task2 = insert("test2")

        delete(task2)
        assertEquals(1, todoListDao.getTasks().size)

        delete(task1)
        assertEquals(0, todoListDao.getTasks().size)
    }

    @Test
    fun testDeleteNonexisting() = runBlocking {
        val task = createTask("task1", 123)
        delete(task)
    }

    @Test
    fun testUpdate() = runBlocking {
        var task = insert("test1")
        task = task.copy(
            title="test1_updated", comments="some comment",
            context=Predefined.TaskContextValues.home,
            status=Predefined.TaskStatusValues.nextAction,
            startDate=LocalDate.of(2000, 4, 10),
            startTime=LocalTime.of(14, 55),
            dueDate=LocalDate.of(2000, 5, 11),
            dueTime=LocalTime.of(16, 56),
            done=true
        )
        update(task)

        val updatedTask = get(task.uid)
        assertEquals(task, updatedTask)
    }

    @Test
    fun testUpdateNonexisting() = runBlocking {
        update(exampleTask.copy(uid = 123))
        assertEquals(getAll().size, 0)
    }

    private fun createTask(title: String, uid: Long=0): TaskEntity {
        return TaskEntity(
            title, "", null, null,
            null, null, null, null,
            false, uid)
    }

    private suspend fun insert(title: String, uid: Long=0): TaskEntity {
        val task = createTask(title, uid)
        return insert(task)
    }

    private suspend fun insert(task: TaskEntity): TaskEntity {
        val newUid = todoListDao.insertTask(task)
        return task.copy(uid=newUid)
    }

    private suspend fun update(task: TaskEntity) {
        todoListDao.updateTask(task)
    }

    private suspend fun get(uid: Long): TaskEntity {
        return todoListDao.getTask(uid)
    }

    private suspend fun getAll(): List<TaskEntity> {
        return todoListDao.getTasks()
    }

    private suspend fun delete(task: TaskEntity) {
        todoListDao.deleteTask(task)
    }

    private val exampleTask = TaskEntity(
        title="example task", comments="some comment",
        context=Predefined.TaskContextValues.home,
        status=Predefined.TaskStatusValues.nextAction,
        startDate=LocalDate.of(2000, 4, 10),
        startTime=LocalTime.of(14, 55),
        dueDate=LocalDate.of(2000, 5, 11),
        dueTime=LocalTime.of(16, 56),
        done=true
    )

}