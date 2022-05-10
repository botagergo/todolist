package hu.botagergo.todolist.group

import android.content.Context
import hu.botagergo.todolist.R
import hu.botagergo.todolist.model.Task
import java.time.LocalDate

class DueGrouper : GrouperBase<Task>() {
    override fun key(item: Task, context: Context): Any {
        if (item.dueDate != null) {
            val today = LocalDate.now().dayOfYear
            val due = item.dueDate.dayOfYear
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
            }
        }
        return "Due later"
    }

    override fun clone(): Grouper<Task> {
        return DueGrouper()
    }

    override fun getName(): Int = R.string.due_date

}