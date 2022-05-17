package hu.botagergo.todolist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class TaskTest {

    @Test
    fun testEquals() {
        assertEquals(Util.task, Util.task.copy())
        assertNotEquals(Util.task.copy(title="other title"), Util.task)
        assertNotEquals(Util.task.copy(comments="other comments"), Util.task)
        assertNotEquals(Util.task.copy(status=Predefined.TaskStatusValues.planning), Util.task)
        assertNotEquals(Util.task.copy(context=Predefined.TaskContextValues.errands), Util.task)
        assertNotEquals(Util.task.copy(startDate=LocalDate.of(2001, 4, 10)), Util.task)
        assertNotEquals(Util.task.copy(startTime=LocalTime.of(17, 46)), Util.task)
        assertNotEquals(Util.task.copy(dueDate=LocalDate.of(2001, 5, 10)), Util.task)
        assertNotEquals(Util.task.copy(dueTime=LocalTime.of(6, 36)), Util.task)
        assertNotEquals(Util.task.copy(done=false), Util.task)
    }

}