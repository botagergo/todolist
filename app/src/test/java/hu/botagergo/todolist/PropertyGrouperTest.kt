package hu.botagergo.todolist

import hu.botagergo.todolist.feature_task_view.data.group.Grouper
import hu.botagergo.todolist.feature_task_view.data.group.PropertyGrouper
import hu.botagergo.todolist.feature_task.data.Task
import org.junit.Test
import org.junit.Assert.assertEquals
import java.lang.RuntimeException
import java.time.LocalTime
import java.util.*

class PropertyGrouperTest {

    @Test
    fun testEmptyGroup() {
        val tasks = mutableListOf<Task>()
        val grouper = PropertyGrouper(Predefined.TaskProperty.title)
        val groups = grouper.group(tasks)
        assertEquals(0, groups.size)
    }

    @Test
    fun testGroup() {
        val ids = mutableListOf(
            Predefined.TaskContextValues.home.name,
            Predefined.TaskContextValues.errands.name,
            Predefined.TaskContextValues.work.name
        ).sortedBy { it }

        var tasks = mutableListOf(
            Util.task.copy(title = "task1"),
            Util.task.copy(title = "task1"),
            Util.task.copy(title = "task1"),
            Util.task.copy(title = "task2"),
            Util.task.copy(title = "task2"),
            Util.task.copy(title = "task3"),
            Util.task.copy(title = "task3"),
        )
        var grouper = PropertyGrouper(Predefined.TaskProperty.title)
        var groups = grouper.group(tasks).sortedBy { it.name as String }
        assertEquals(3, groups.size)
        assertEquals("task1", groups[0].name)
        assertEquals(3, groups[0].items.size)
        assertEquals("task2", groups[1].name)
        assertEquals(2, groups[1].items.size)
        assertEquals("task3", groups[2].name)
        assertEquals(2, groups[2].items.size)

        tasks = mutableListOf(
            Util.task.copy(done = true),
            Util.task.copy(done = false),
            Util.task.copy(done = true),
            Util.task.copy(done = false),
        )
        grouper = PropertyGrouper(Predefined.TaskProperty.done)
        groups = grouper.group(tasks).sortedBy { it.name as String }
        assertEquals(2, groups.size)
        assertEquals("false", groups[0].name)
        assertEquals(2, groups[0].items.size)
        assertEquals("true", groups[1].name)
        assertEquals(2, groups[1].items.size)

        tasks = mutableListOf(
            Util.task.copy(context = Predefined.TaskContextValues.errands),
            Util.task.copy(context = Predefined.TaskContextValues.errands),
            Util.task.copy(context = Predefined.TaskContextValues.home),
            Util.task.copy(context = Predefined.TaskContextValues.home),
            Util.task.copy(context = Predefined.TaskContextValues.work),
            Util.task.copy(context = Predefined.TaskContextValues.work),
        )
        grouper = PropertyGrouper(Predefined.TaskProperty.context)
        groups = grouper.group(tasks).sortedBy { it.name as Int }
        assertEquals(3, groups.size)
        assertEquals(ids[0], groups[0].name)
        assertEquals(2, groups[0].items.size)
        assertEquals(ids[1], groups[1].name)
        assertEquals(2, groups[1].items.size)
        assertEquals(ids[2], groups[2].name)
        assertEquals(2, groups[2].items.size)

        tasks = mutableListOf(
            Util.task.copy(dueTime = LocalTime.of(10, 22)),
            Util.task.copy(dueTime = LocalTime.of(10, 22)),
            Util.task.copy(dueTime = LocalTime.of(11, 22)),
            Util.task.copy(dueTime = LocalTime.of(11, 22)),
        )
        grouper = PropertyGrouper(Predefined.TaskProperty.dueTime)
        groups = grouper.group(tasks).sortedBy { it.name as String }
        assertEquals(2, groups.size)
        assertEquals(2, groups[0].items.size)
        assertEquals(2, groups[1].items.size)
    }

    @Test
    fun testNullKey() {
        val tasks = mutableListOf(
            Util.task.copy(context = Predefined.TaskContextValues.errands),
            Util.task.copy(context = Predefined.TaskContextValues.errands),
            Util.task.copy(context = null),
            Util.task.copy(context = null),
        )

        var grouper = PropertyGrouper(Predefined.TaskProperty.context)
        try {
            grouper.group(tasks).sortedBy { it.name as Int }
            assert(false)
        } catch (e: RuntimeException) {
            assert(true)
        }

        grouper = PropertyGrouper(Predefined.TaskProperty.context, "none")
        val groups: List<Grouper.Group<Task>> = grouper.group(tasks)

        assertEquals(2, groups.size)

        if (groups[1].name == "none") {
            Collections.swap(groups, 0, 1)
        }

        assertEquals(groups[0].name, "none")
        assertEquals(2, groups[0].items.size)
        assertEquals(2, groups[1].items.size)
    }

}