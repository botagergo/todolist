package hu.botagergo.todolist.feature_task_view.data.model

import hu.botagergo.todolist.feature_task_view.domain.model.filter.Filter
import hu.botagergo.todolist.feature_task_view.data.group.Grouper
import hu.botagergo.todolist.feature_task_view.data.sorter.Sorter
import hu.botagergo.todolist.core.util.UUIDOwner
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class TaskView(
    var name: String,
    var description: String?,
    var filter: Filter<TaskEntity>?,
    var grouper: Grouper<TaskEntity>?,
    var sorter: Sorter<TaskEntity>?,
    uuid: UUID
) : UUIDOwner(uuid), Serializable, Cloneable {

    constructor(
        name: String,
        description: String?,
        filter: Filter<TaskEntity>?,
        grouper: Grouper<TaskEntity>?,
        sorter: Sorter<TaskEntity>?
    ) : this(name, description, filter, grouper, sorter, UUID.randomUUID())

    class Builder(val name: String) {

        private var _description: String? = null
        private var _filter: Filter<TaskEntity>? = null
        private var _grouper: Grouper<TaskEntity>? = null
        private var _sorter: Sorter<TaskEntity>? = null

        fun description(description: String?): Builder {
            _description = description
            return this
        }

        fun filter(filter: Filter<TaskEntity>): Builder {
            _filter = filter
            return this
        }

        fun grouper(grouper: Grouper<TaskEntity>): Builder {
            _grouper = grouper
            return this
        }

        fun sorter(sorter: Sorter<TaskEntity>): Builder {
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

    public override fun clone(): TaskView {
        return TaskView(name, description, filter?.clone(), grouper?.clone(), sorter?.clone(), uuid)
    }

    override fun equals(other: Any?): Boolean {
        return (other as? TaskView)?.uuid == uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

}