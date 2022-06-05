package hu.botagergo.todolist.feature_task_view.mapper

import android.content.Context
import hu.botagergo.todolist.feature_task.data.model.*
import hu.botagergo.todolist.feature_task.mapper.toPropertyInfo
import hu.botagergo.todolist.feature_task.mapper.toTaskProperty
import hu.botagergo.todolist.feature_task_view.data.model.PredicateEntity
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterInfo
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterEntity
import hu.botagergo.todolist.feature_task_view.domain.model.filter.PropertyFilter

fun toTaskFilter(
    filter: TaskFilterEntity,
    property: PropertyEntity,
    predicate: PredicateEntity,
    enumValue: TaskEnumPropertyValueEntity?,
    context: Context
): PropertyFilter<Task> {

}