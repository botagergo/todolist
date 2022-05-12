package hu.botagergo.todolist

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskDao
import hu.botagergo.todolist.model.TaskDatabase
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class TaskDatabaseTest {

    private lateinit var context: Context
    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun initDb() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        taskDao = db.taskDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGet() {
        val task1 = insert(exampleTask)
        val task2 = get(task1.uid)
        assertEquals(task1, task2)
    }

    @Test
    fun testInsert() {
        insert("test1")
        insert("test2")
        assertEquals(2, taskDao.getAll().size)
        insert("test3")
        assertEquals(3, taskDao.getAll().size)
    }

    @Test
    fun testInsertWithSpecificUid() {
        insert("test1", 123)
        val task = get(123)
    }

    @Test
    fun testInsertExisting() {
        val task1 = insert("test1")
        try {
            insert("test2", task1.uid)
            assert(false)
        } catch (e: SQLiteConstraintException) {
            assert(true)
        }
    }

    @Test
    fun testDelete() {
        val task1 = insert("test1")
        val task2 = insert("test2")

        delete(task2)
        assertEquals(1, taskDao.getAll().size)

        delete(task1)
        assertEquals(0, taskDao.getAll().size)
    }

    @Test
    fun testDeleteNonexisting() {
        val task = createTask("task1", 123)
        delete(task)
    }

    @Test
    fun testUpdate() {
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
    fun testUpdateNonexisting() {
        update(exampleTask.copy(uid=123))
        assertEquals(getAll().size, 0)
    }

    private fun createTask(title: String, uid: Long=0): Task {
        return Task(
            title, "", null, null,
            null, null, null, null,
            false, uid)
    }

    private fun insert(title: String, uid: Long=0): Task {
        val task = createTask(title, uid)
        return insert(task)
    }

    private fun insert(task: Task): Task {
        val newUid = taskDao.insert(task)
        return task.copy(uid=newUid)
    }

    private fun update(task: Task) {
        taskDao.update(task)
    }

    private fun get(uid: Long): Task {
        return taskDao.get(uid)
    }

    private fun getAll(): List<Task> {
        return taskDao.getAll()
    }

    private fun delete(task: Task) {
        taskDao.delete(task)
    }

    private val exampleTask = Task(
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