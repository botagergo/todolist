package hu.botagergo.todolist.feature_task.data

import androidx.room.*
import hu.botagergo.todolist.core.util.EnumValue
import hu.botagergo.todolist.core.util.EnumValueIntConverter
import java.time.LocalDate
import java.time.LocalTime

@Entity
@TypeConverters(
    LocalDateConverter::class, LocalTimeConverter::class,
    EnumValueIntConverter::class
)
data class Task(
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "comments") val comments: String = "",
    @ColumnInfo(name = "status") val status: EnumValue? = null,
    @ColumnInfo(name = "context") val context: EnumValue? = null,
    @ColumnInfo(name = "startDate") val startDate: LocalDate? = null,
    @ColumnInfo(name = "startTime") val startTime: LocalTime? = null,
    @ColumnInfo(name = "dueDate") val dueDate: LocalDate? = null,
    @ColumnInfo(name = "dueTime") val dueTime: LocalTime? = null,
    @ColumnInfo(name = "done") val done: Boolean = false,
    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)