package hu.botagergo.todolist.feature_task.data.model

import androidx.room.TypeConverter
import java.time.LocalTime

object LocalTimeConverter {
    @TypeConverter
    fun toTime(timeString: String?): LocalTime? {
        return timeString?.let {
            LocalTime.parse(it)
        }
    }

    @TypeConverter
    fun toTimeString(time: LocalTime?): String? {
        return time?.toString()
    }
}