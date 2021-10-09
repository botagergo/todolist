package hu.botagergo.todolist.model

import androidx.room.TypeConverter
import java.time.LocalTime

object LocalTimeConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalTime? {
        return if (dateString == null) {
            null
        } else {
            LocalTime.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalTime?): String? {
        return date?.toString()
    }
}