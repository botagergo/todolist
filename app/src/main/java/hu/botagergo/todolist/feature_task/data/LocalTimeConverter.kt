package hu.botagergo.todolist.feature_task.data

import androidx.room.TypeConverter
import java.time.LocalTime

object LocalTimeConverter {
    @TypeConverter
    fun toTime(timeString: String?): LocalTime? {
        return if (timeString == null) {
            null
        } else {
            LocalTime.parse(timeString)
        }
    }

    @TypeConverter
    fun toTimeString(time: LocalTime?): String? {
        return time?.toString()
    }
}