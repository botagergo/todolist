package hu.botagergo.todolist.feature_task.domain.repository

import hu.botagergo.todolist.core.util.EnumValue
import hu.botagergo.todolist.feature_task.data.model.TaskEnumPropertyValueEntity
import kotlinx.coroutines.flow.Flow

interface TaskEnumPropertyValueRepository {

    fun getTaskEnumPropertyValues(propertyId: String): Flow<List<EnumValue>>
    suspend fun insertTaskEnumPropertyValue(taskEnumPropertyValue: TaskEnumPropertyValueEntity)
}