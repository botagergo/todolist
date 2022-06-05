package hu.botagergo.todolist.feature_task.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.botagergo.todolist.core.util.EnumValue
import hu.botagergo.todolist.data.TodoListDao
import hu.botagergo.todolist.feature_task.data.model.TaskEnumPropertyValueEntity
import hu.botagergo.todolist.feature_task.domain.repository.TaskEnumPropertyValueRepository
import hu.botagergo.todolist.feature_task.mapper.toTaskEnumValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskEnumPropertyValueRepositoryImpl
    @Inject constructor(
        @ApplicationContext private val context: Context,
        private val todoListDao: TodoListDao
    ): TaskEnumPropertyValueRepository {

    override fun getTaskEnumPropertyValues(propertyId: String): Flow<List<EnumValue>> {
        return todoListDao.getTaskEnumPropertyValues(propertyId).map { it ->
            it.map { it.toTaskEnumValue(context) }
        }
    }

    override suspend fun insertTaskEnumPropertyValue(taskEnumPropertyValue: TaskEnumPropertyValueEntity) {
        todoListDao.insertTaskEnumPropertyValue(taskEnumPropertyValue)
    }

}