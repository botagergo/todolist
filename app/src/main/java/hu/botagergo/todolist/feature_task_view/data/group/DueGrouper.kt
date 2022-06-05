package hu.botagergo.todolist.feature_task_view.data.group

import hu.botagergo.todolist.R
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import java.time.LocalDate

class DueGrouper : GrouperBase<TaskEntity>() {

    override fun key(item: TaskEntity): Any {
        return item.dueDate?.let {
            val today = LocalDate.now().dayOfYear
            val due = it.dayOfYear
            when {
                due == today -> {
                    return "Due today"
                }
                due == today + 1 -> {
                    return "Due tomorrow"
                }
                due < today + 7 -> {
                    return "Due in the next week"
                }
                else -> "Due later"
            }
        } ?: "Due later"
    }

    override fun clone(): Grouper<TaskEntity> {
        return DueGrouper()
    }

    override val name: Int = R.string.due_date

}