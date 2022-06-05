package hu.botagergo.todolist.feature_task_view.data.model

import hu.botagergo.todolist.core.util.FilterKind
import hu.botagergo.todolist.feature_task.data.model.PropertyInfo

data class TaskFilterInfo (
    val kind: FilterKind,
    val propertyInfo: PropertyInfo?,
    val predicate: Predicate?,
    val operand: Any?,
    val children: List<TaskFilterInfo>?,
    val id: Long
) {

}
