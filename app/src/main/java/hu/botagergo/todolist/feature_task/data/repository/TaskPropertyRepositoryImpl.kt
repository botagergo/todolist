package hu.botagergo.todolist.feature_task.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.botagergo.todolist.core.util.Property
import hu.botagergo.todolist.data.TodoListDao
import hu.botagergo.todolist.feature_task.data.model.PropertyEntity
import hu.botagergo.todolist.feature_task.data.model.Task
import hu.botagergo.todolist.feature_task.domain.repository.TaskPropertyRepository
import hu.botagergo.todolist.feature_task.mapper.toTaskProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskPropertyRepositoryImpl
    @Inject constructor(
        @ApplicationContext private val context: Context,
        private val todoListDao: TodoListDao
        ): TaskPropertyRepository {

    override suspend fun getTaskProperty(id: String): Property<Task> {
        return todoListDao.getTaskProperty(id).toTaskProperty(context)
    }

    override fun getTaskProperties(): Flow<List<Property<Task>>> {
        return todoListDao.getTaskProperties().map { it ->
            it.map { it.toTaskProperty(context) }
        }
    }

    override suspend fun insertTaskProperty(propertyEntity: PropertyEntity) {
        todoListDao.insertTaskProperty(propertyEntity)
    }

}