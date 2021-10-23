package hu.botagergo.todolist.group

import hu.botagergo.todolist.model.Task
import java.time.LocalDate

class DueGrouper : GrouperBase<Task, Any?>() {
    override fun key(item: Task): Any {
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
}