package hu.botagergo.todolist.feature_task.data.model

import androidx.room.*
import hu.botagergo.todolist.core.util.EnumValue
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "task"
)
@TypeConverters(
    LocalDateConverter::class, LocalTimeConverter::class
)
data class TaskEntity(
    val title: String = "",
    val comments: String = "",
    val status: EnumValue? = null,
    val context: EnumValue? = null,
    val startDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val done: Boolean = false,
    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)