package hu.botagergo.todolist.feature_task.domain.repository

import hu.botagergo.todolist.core.util.Property
import hu.botagergo.todolist.feature_task.data.model.PropertyEntity
import hu.botagergo.todolist.feature_task.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskPropertyRepository {

    suspend fun getTaskProperty(id: String): Property<Task>
    fun getTaskProperties(): Flow<List<Property<Task>>>
    suspend fun insertTaskProperty(propertyEntity: PropertyEntity)

}