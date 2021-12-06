package hu.botagergo.todolist.model

import hu.botagergo.todolist.filter.Filter
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.sorter.Sorter
import hu.botagergo.todolist.util.UUIDOwner
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class TaskView(
    val name: String,
    val description: String?,
    val filter: Filter<Task>?,
    val grouper: Grouper<Task, Any?>?,
    val sorter: Sorter<Task>?,
    uuid: UUID
) : UUIDOwner(uuid), Serializable, Cloneable {

    constructor(
        name: String,
        description: String?,
        filter: Filter<Task>?,
        grouper: Grouper<Task, Any?>?,
        sorter: Sorter<Task>?
    ) : this(name, description, filter, grouper, sorter, UUID.randomUUID())

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
            return TaskView(name, _description, _filter, _grouper, _sorter)
        }

    }

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

}