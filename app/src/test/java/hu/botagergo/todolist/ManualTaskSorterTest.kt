package hu.botagergo.todolist

import hu.botagergo.todolist.feature_task_view.data.sorter.ManualTaskSorter
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ManualTaskSorterTest {

    @Test
    fun testExstingUid() {
        val sorter = ManualTaskSorter()
        assertTrue(sorter.uids.isEmpty())

        val tasks = arrayListOf(
            Util.task.copy(uid=1, title = "task1"),
            Util.task.copy(uid=2, title = "task2"),
            Util.task.copy(uid=3, title = "task3"),
            Util.task.copy(uid=4, title = "task4"),
        )

        sorter.sort(tasks)
        assertEquals(4, sorter.uids.size)
        assertEquals("task1", tasks[0].title)
        assertEquals("task2", tasks[1].title)
        assertEquals("task3", tasks[2].title)
        assertEquals("task4", tasks[3].title)

        Collections.swap(tasks, 0, 2)
        sorter.sort(tasks)
        assertEquals(4, sorter.uids.size)
        assertEquals("task1", tasks[0].title)
        assertEquals("task2", tasks[1].title)
        assertEquals("task3", tasks[2].title)
        assertEquals("task4", tasks[3].title)

        Collections.swap(sorter.uids, 0, 2)
        sorter.sort(tasks)
        assertEquals(4, sorter.uids.size)
        assertEquals("task3", tasks[0].title)
        assertEquals("task2", tasks[1].title)
        assertEquals("task1", tasks[2].title)
        assertEquals("task4", tasks[3].title)

        sorter.uids = arrayListOf(tasks[1].uid, tasks[3].uid)
        sorter.sort(tasks)
        assertEquals(4, sorter.uids.size)
        assertEquals("task2", tasks[0].title)
        assertEquals("task4", tasks[1].title)
        assertEquals("task3", tasks[2].title)
        assertEquals("task1", tasks[3].title)
    }

    @Test
    fun testUnexistingUid() {
        val sorter = ManualTaskSorter()
        val tasks = arrayListOf(
            Util.task.copy(uid=1, title = "task1"),
            Util.task.copy(uid=2, title = "task2"),
            Util.task.copy(uid=3, title = "task3"),
            Util.task.copy(uid=4, title = "task4"),
        )

        sorter.uids = arrayListOf(9, 2, 10)
        sorter.sort(tasks)
        assertEquals(4, sorter.uids.size)
        assertEquals("task2", tasks[0].title)
        assertEquals("task1", tasks[1].title)
        assertEquals("task3", tasks[2].title)
        assertEquals("task4", tasks[3].title)
    }

    @Test
    fun testClone() {
        val sorter = ManualTaskSorter(arrayListOf(2, 4, 6))
        assertEquals(sorter, sorter.clone())
    }

}