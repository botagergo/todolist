package hu.botagergo.todolist

import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task_view.data.sorter.CompositeSorter
import hu.botagergo.todolist.feature_task_view.data.sorter.PropertySortCriterion
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalTime

class CompositeSorterTest {

    @Test
    fun testEmptyCriterionList() {
        try {
            CompositeSorter<TaskEntity>(
                mutableListOf()
            )
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
        }
    }

    @Test
    fun testSort() {
        val sorter = CompositeSorter(
            mutableListOf(
                PropertySortCriterion(Predefined.TaskProperty.done),
                PropertySortCriterion(Predefined.TaskProperty.dueTime),
                PropertySortCriterion(Predefined.TaskProperty.title)
            )
        )

        val tasks = arrayListOf(
            Util.task.copy(done = false, dueTime = LocalTime.of(11, 22), title = "task4"),
            Util.task.copy(done = true, dueTime = LocalTime.of(13, 22), title = "task8"),
            Util.task.copy(done = false, dueTime = LocalTime.of(11, 22), title = "task3"),
            Util.task.copy(done = true, dueTime = LocalTime.of(13, 22), title = "task9"),
            Util.task.copy(done = false, dueTime = LocalTime.of(11, 22), title = "task5"),
            Util.task.copy(done = true, dueTime = LocalTime.of(12, 22), title = "task6"),
            Util.task.copy(done = false, dueTime = LocalTime.of(10, 22), title = "task2"),
            Util.task.copy(done = false, dueTime = LocalTime.of(10, 22), title = "task1"),
            Util.task.copy(done = true, dueTime = LocalTime.of(12, 22), title = "task7"),
        )

        sorter.sort(tasks)

        assertEquals("task1", tasks[0].title)
        assertEquals("task2", tasks[1].title)
        assertEquals("task3", tasks[2].title)
        assertEquals("task4", tasks[3].title)
        assertEquals("task5", tasks[4].title)
        assertEquals("task6", tasks[5].title)
        assertEquals("task7", tasks[6].title)
        assertEquals("task8", tasks[7].title)
        assertEquals("task9", tasks[8].title)

    }

    @Test
    fun testSortEmpty() {
        val tasks = arrayListOf<TaskEntity>()
        val sorter = CompositeSorter(
            mutableListOf(
                PropertySortCriterion(Predefined.TaskProperty.done),
                PropertySortCriterion(Predefined.TaskProperty.dueTime),
                PropertySortCriterion(Predefined.TaskProperty.title)
            )
        )
        sorter.sort(tasks)
    }
}