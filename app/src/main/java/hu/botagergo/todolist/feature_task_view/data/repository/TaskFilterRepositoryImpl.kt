package hu.botagergo.todolist.feature_task_view.data.repository

import android.content.Context
import hu.botagergo.todolist.core.util.FilterKind
import hu.botagergo.todolist.core.util.PropertyType
import hu.botagergo.todolist.data.TodoListDao
import hu.botagergo.todolist.feature_task.data.model.LocalDateConverter
import hu.botagergo.todolist.feature_task.data.model.LocalTimeConverter
import hu.botagergo.todolist.feature_task.data.model.Task
import hu.botagergo.todolist.feature_task.data.model.TaskEnumPropertyValueEntity
import hu.botagergo.todolist.feature_task.mapper.toPropertyInfo
import hu.botagergo.todolist.feature_task.mapper.toTaskEnumValue
import hu.botagergo.todolist.feature_task.mapper.toTaskProperty
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterEntity
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterInfo
import hu.botagergo.todolist.feature_task_view.domain.TaskFilterRepository
import hu.botagergo.todolist.feature_task_view.domain.model.filter.AndFilter
import hu.botagergo.todolist.feature_task_view.domain.model.filter.Filter
import hu.botagergo.todolist.feature_task_view.domain.model.filter.OrFilter
import hu.botagergo.todolist.feature_task_view.domain.model.filter.PropertyFilter
import hu.botagergo.todolist.feature_task_view.mapper.toPredicate
import hu.botagergo.todolist.feature_task_view.mapper.toPredicateInfo
import java.lang.IllegalArgumentException
import javax.inject.Inject

internal fun convertOperand(
    operand: String, propertyType: PropertyType,
    enumValue: TaskEnumPropertyValueEntity?, context: Context
): Any {
    return when (propertyType) {
        PropertyType.STRING -> operand
        PropertyType.BOOLEAN -> operand.toBoolean()
        PropertyType.DATE -> LocalDateConverter.toDate(operand)!!
        PropertyType.TIME -> LocalTimeConverter.toTime(operand)!!
        PropertyType.ENUM -> enumValue!!.toTaskEnumValue(context)
    }
}

class TaskFilterRepositoryImpl
@Inject constructor(
    private val context: Context, private val todoListDao: TodoListDao
) : TaskFilterRepository {


    override suspend fun getInfo(id: Long): TaskFilterInfo {
        val filter = todoListDao.getTaskFilter(id)
        return getInfo(filter)
    }

    private suspend fun getInfo(taskFilterEntity: TaskFilterEntity): TaskFilterInfo {
        if (taskFilterEntity.kind == FilterKind.PROPERTY) {
            val property =
                todoListDao.getTaskProperty(taskFilterEntity.propertyId!!).toPropertyInfo(context)
            val predicate =
                todoListDao.getPredicate(taskFilterEntity.predicateId!!).toPredicateInfo(context)
            val enumValue = if (property.type == PropertyType.ENUM)
                todoListDao.getTaskEnumPropertyValue(
                    property.id,
                    taskFilterEntity.operand.toLong()
                ) else null
            val operand =
                convertOperand(taskFilterEntity.operand, property.type, enumValue, context)

            return TaskFilterInfo(
                taskFilterEntity.kind,
                property,
                predicate,
                operand,
                null,
                taskFilterEntity.id
            )

        } else {
            val children = todoListDao.getTaskFilterChildren(taskFilterEntity.id).map {
                getInfo(it)
            }

            return TaskFilterInfo(
                taskFilterEntity.kind,
                null,
                null,
                null,
                children,
                taskFilterEntity.id
            )
        }
    }

    override suspend fun get(id: Long): Filter<Task> {
        val filter = todoListDao.getTaskFilter(id)
        return get(filter)
    }

    suspend fun get(taskFilterEntity: TaskFilterEntity): Filter<Task> {
        if (taskFilterEntity.kind == FilterKind.PROPERTY) {
            val propertyInfo = todoListDao.getTaskProperty(taskFilterEntity.propertyId!!)
            val property = propertyInfo.toTaskProperty(context)
            val predicate = todoListDao.getPredicate(taskFilterEntity.predicateId!!).toPredicate()
            val enumValue = if (property.type == PropertyType.ENUM)
                todoListDao.getTaskEnumPropertyValue(
                    property.id,
                    taskFilterEntity.operand.toLong()
                ) else null
            val operand = convertOperand(taskFilterEntity.operand, property.type, enumValue, context)

            return PropertyFilter(
                property,
                predicate,
                operand,
                false,
                taskFilterEntity.id
            )
        } else {
            val children = todoListDao.getTaskFilterChildren(taskFilterEntity.id).map {
                get(it)
            }.toTypedArray()

            return when (taskFilterEntity.kind) {
                FilterKind.AND -> {
                    AndFilter(taskFilterEntity.id, *children)
                }
                FilterKind.OR -> {
                    OrFilter(taskFilterEntity.id, *children)
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }


    override suspend fun insert(filter: TaskFilterInfo) {
        filter.
    }

}