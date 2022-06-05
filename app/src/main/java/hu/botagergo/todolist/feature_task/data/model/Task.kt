package hu.botagergo.todolist.feature_task.data.model

import hu.botagergo.todolist.core.util.EnumValue
import java.time.LocalDate
import java.time.LocalTime

data class Task(
    val title: String,
    val comments: String,
    val status: EnumValue?,
    val context: EnumValue?,
    val startDate: LocalDate?,
    val startTime: LocalTime?,
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val done: Boolean,
    val uid: Long = 0
)