package hu.botagergo.todolist

import hu.botagergo.todolist.sorter.PropertySortCriterion
import hu.botagergo.todolist.sorter.SortCriterion
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class PropertySortCriterionTest {

    @Test
    fun testPropertyTypes() {
        var criterion = PropertySortCriterion(Predefined.TaskProperty.title)
        assertEquals(-1, criterion.comparator.compare(Util.task.copy(title = "task1"), Util.task.copy(title = "task2")))
        assertEquals(0, criterion.comparator.compare(Util.task.copy(title = "task1"), Util.task.copy(title = "task1")))
        assertEquals(1, criterion.comparator.compare(Util.task.copy(title = "task2"), Util.task.copy(title = "task1")))

        criterion = PropertySortCriterion(Predefined.TaskProperty.done)
        assertEquals(-1, criterion.comparator.compare(Util.task.copy(done = false), Util.task.copy(done = true)))
        assertEquals(0, criterion.comparator.compare(Util.task.copy(done = false), Util.task.copy(done = false)))
        assertEquals(1, criterion.comparator.compare(Util.task.copy(done = true), Util.task.copy(done = false)))

        criterion = PropertySortCriterion(Predefined.TaskProperty.dueDate)
        assertEquals(-1, criterion.comparator.compare(Util.task.copy(dueDate = LocalDate.of(2000, 10, 3)), Util.task.copy(dueDate = LocalDate.of(2001, 10, 3))))
        assertEquals(0, criterion.comparator.compare(Util.task.copy(dueDate = LocalDate.of(2000, 10, 3)), Util.task.copy(dueDate = LocalDate.of(2000, 10, 3))))
        assertEquals(1, criterion.comparator.compare(Util.task.copy(dueDate = LocalDate.of(2001, 10, 3)), Util.task.copy(dueDate = LocalDate.of(2000, 10, 3))))

        criterion = PropertySortCriterion(Predefined.TaskProperty.dueTime)
        assertEquals(-1, criterion.comparator.compare(Util.task.copy(dueTime = LocalTime.of(10, 15)), Util.task.copy(dueTime = LocalTime.of(11, 15))))
        assertEquals(0, criterion.comparator.compare(Util.task.copy(dueTime = LocalTime.of(10, 15)), Util.task.copy(dueTime = LocalTime.of(10, 15))))
        assertEquals(1, criterion.comparator.compare(Util.task.copy(dueTime = LocalTime.of(11, 15)), Util.task.copy(dueTime = LocalTime.of(10, 15))))
    }

    @Test
    fun testAscendingDescending() {
        val task1 = Util.task.copy(title = "task1")
        val task2 = Util.task.copy(title = "task2")

        var criterion = PropertySortCriterion(Predefined.TaskProperty.title, SortCriterion.Order.ASCENDING)
        assertEquals(-1, criterion.comparator.compare(task1, task2))
        assertEquals(0, criterion.comparator.compare(task1, task1.copy()))

        criterion = criterion.copy(order = SortCriterion.Order.DESCENDING)
        assertEquals(1, criterion.comparator.compare(task1, task2))
        assertEquals(0, criterion.comparator.compare(task1, task1.copy()))
    }

    @Test
    fun testNulls() {

        var comp = PropertySortCriterion(Predefined.TaskProperty.dueTime, SortCriterion.Order.ASCENDING, true).comparator
        assertEquals(-1, comp.compare(Util.task.copy(dueTime = LocalTime.of(10, 15)), Util.task.copy(dueTime = null)))
        assertEquals(0, comp.compare(Util.task.copy(dueTime = null), Util.task.copy(dueTime = null)))
        assertEquals(1, comp.compare(Util.task.copy(dueTime = null), Util.task.copy(dueTime = LocalTime.of(10, 15))))

        comp = PropertySortCriterion(Predefined.TaskProperty.dueTime, SortCriterion.Order.ASCENDING, false).comparator
        assertEquals(-1, comp.compare(Util.task.copy(dueTime = null), Util.task.copy(dueTime = LocalTime.of(10, 15))))
        assertEquals(0, comp.compare(Util.task.copy(dueTime = null), Util.task.copy(dueTime = null)))
        assertEquals(1, comp.compare(Util.task.copy(dueTime = LocalTime.of(10, 15)), Util.task.copy(dueTime = null)))

        comp = PropertySortCriterion(Predefined.TaskProperty.dueTime, SortCriterion.Order.DESCENDING, true).comparator
        assertEquals(-1, comp.compare(Util.task.copy(dueTime = LocalTime.of(10, 15)), Util.task.copy(dueTime = null)))
        assertEquals(0, comp.compare(Util.task.copy(dueTime = null), Util.task.copy(dueTime = null)))
        assertEquals(1, comp.compare(Util.task.copy(dueTime = null), Util.task.copy(dueTime = LocalTime.of(10, 15))))

        comp = PropertySortCriterion(Predefined.TaskProperty.dueTime, SortCriterion.Order.DESCENDING, false).comparator
        assertEquals(-1, comp.compare(Util.task.copy(dueTime = null), Util.task.copy(dueTime = LocalTime.of(10, 15))))
        assertEquals(0, comp.compare(Util.task.copy(dueTime = null), Util.task.copy(dueTime = null)))
        assertEquals(1, comp.compare(Util.task.copy(dueTime = LocalTime.of(10, 15)), Util.task.copy(dueTime = null)))


    }

}