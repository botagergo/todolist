package hu.botagergo.todolist.feature_task.data.model

import androidx.room.TypeConverter
import java.time.LocalDate

object LocalDateConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return dateString?.let {
            LocalDate.parse(it)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return date?.toString()
    }
}