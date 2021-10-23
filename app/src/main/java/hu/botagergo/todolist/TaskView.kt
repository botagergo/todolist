package hu.botagergo.todolist

import hu.botagergo.todolist.filter.*
import hu.botagergo.todolist.group.DueGrouper
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.group.PropertyGrouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.Sorter
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import java.io.Serializable
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class TaskView private constructor(
    val name: String,
    val description: String?,
    val filter: Filter<Task>?,
    val grouper: Grouper<Task, Any?>?,
    val sorter: Sorter<Task>?
) : Serializable {

    class Builder(val name: String) {

        private var _description: String? = null
        private var _filter: Filter<Task>? = null
        private var _grouper: Grouper<Task, Any?>? = null
        private var _sorter: Sorter<Task>? = null

        fun description(description: String?): Builder {
            _description = description
            return this
        }

        fun filter(filter: Filter<Task>): Builder {
            _filter = filter
            return this
        }

        fun grouper(grouper: Grouper<Task, Any?>): Builder {
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
        var groupOrder: MutableList<Any?> = ArrayList()
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

        val hotlist by lazy {
            Builder("Hotlist")
                .filter(
                    ConjugateFilter(
                        PropertyEqualsFilter(Task::done, false),
                        PropertyLessEqualFilter(Task::dueDate, { LocalDate.now().plusDays(3) } as (()->LocalDate))
                    ))
                .build()
        }

        val allGroupedByStatus by lazy {
            Builder("All Tasks")
                .description("Show all tasks grouped by status")
                .grouper(
                    PropertyGrouper(Task::status, "None")
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
                    ConjugateFilter(
                        PropertyInFilter(Task::status, setOf(Task.Status("Next Action"))),
                        PropertyEqualsFilter(Task::done, false)
                    )
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
                    PropertyEqualsFilter(Task::done, false)
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }

    }

}