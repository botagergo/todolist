package hu.botagergo.todolist

import hu.botagergo.todolist.model.LocalDateConverter
import hu.botagergo.todolist.model.LocalTimeConverter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class LocalDateTimeTest {

    @Test
    fun testLocalDate() {
        val date = LocalDate.of(2000, 10, 4)
        val dateStr = LocalDateConverter.toDateString(date)
        assertEquals(date, LocalDateConverter.toDate(dateStr))

        assertEquals(null, LocalDateConverter.toDateString(null))
        assertEquals(null, LocalDateConverter.toDate(null))
    }

    @Test
    fun testLocalTime() {
        val time = LocalTime.of(19, 23)
        val dateStr = LocalTimeConverter.toTimeString(time)
        assertEquals(time, LocalTimeConverter.toTime(dateStr))

        assertEquals(null, LocalTimeConverter.toTimeString(null))
        assertEquals(null, LocalTimeConverter.toTime(null))
    }

}