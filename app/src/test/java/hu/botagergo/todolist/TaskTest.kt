package hu.botagergo.todolist

import hu.botagergo.todolist.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class TaskTest {

    @Test
    fun testEquals() {
        assertEquals(exampleTask, exampleTask.copy())
        assertNotEquals(exampleTask.copy(title="other title"), exampleTask)
        assertNotEquals(exampleTask.copy(comments="other comments"), exampleTask)
        assertNotEquals(exampleTask.copy(status=Predefined.TaskStatusValues.planning), exampleTask)
        assertNotEquals(exampleTask.copy(context=Predefined.TaskContextValues.errands), exampleTask)
        assertNotEquals(exampleTask.copy(startDate=LocalDate.of(2001, 4, 10)), exampleTask)
        assertNotEquals(exampleTask.copy(startTime=LocalTime.of(17, 46)), exampleTask)
        assertNotEquals(exampleTask.copy(dueDate=LocalDate.of(2001, 5, 10)), exampleTask)
        assertNotEquals(exampleTask.copy(dueTime=LocalTime.of(6, 36)), exampleTask)
        assertNotEquals(exampleTask.copy(done=false), exampleTask)
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