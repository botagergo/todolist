package hu.botagergo.todolist

import hu.botagergo.todolist.group.DueGrouper
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.group.PropertyGrouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.Sorter
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import hu.botagergo.todolist.task_filter.ConjugateTaskFilter
import hu.botagergo.todolist.task_filter.DoneTaskFilter
import hu.botagergo.todolist.task_filter.StatusTaskFilter
import hu.botagergo.todolist.task_filter.TaskFilter
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class TaskView private constructor(
    val name: String,
    val description: String?,
    val filter: TaskFilter?,
    val grouper: Grouper<Any, Task>?,
    val sorter: Sorter<Task>?
) : Serializable {

    class Builder(val name: String) {

        private var _description: String? = null
        private var _filter: TaskFilter? = null
        private var _grouper: Grouper<Any, Task>? = null
        private var _sorter: Sorter<Task>? = null

        fun description(description: String?): Builder {
            _description = description
            return this
        }

        fun filter(filter: TaskFilter): Builder {
            _filter = filter
            return this
        }

        fun grouper(grouper: Grouper<Any, Task>): Builder {
            _grouper = grouper
            return this
        }

        fun sorter(sorter: Sorter<Task>): Builder {
            _sorter = sorter
            return this
        }

        fun build(): TaskView {
            return TaskView(name, _description, _filter, _grouper, _sorter).apply {
                uuid = UUID.randomUUID()
            }
        }

    }

    lateinit var uuid: UUID
        private set

    class TaskViewState : Serializable {
        var groupExpanded: MutableMap<String, Boolean> = LinkedHashMap()
        var groupOrder: MutableList<Any> = ArrayList()
        var taskOrder: List<Long> = ArrayList()
    }

    var state: TaskViewState = TaskViewState()

    override fun equals(other: Any?): Boolean {
        return (other as? TaskView)?.uuid == uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    companion object Predefined {

        val all by lazy {
            Builder("All Tasks")
                .description("Show all tasks ungrouped")
                .filter(
                    ConjugateTaskFilter()
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }

        val allGroupedByStatus by lazy {
            Builder("All Tasks")
                .description("Show all tasks grouped by status")
                .grouper(
                    PropertyGrouper(Task::status)
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }

        val nextAction by lazy {
            Builder("Next Action")
                .description("Show tasks with status 'Next Action'")
                .filter(
                    StatusTaskFilter(setOf(Task.Status.NextAction))
                )
                .grouper(
                    DueGrouper()
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }

        val done by lazy {
            Builder("Done")
                .description("Show completed tasks")
                .filter(
                    DoneTaskFilter(showDone = true, showNotDone = false)
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }

    }

}