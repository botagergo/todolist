package hu.botagergo.todolist.feature_task.domain.repository

import hu.botagergo.todolist.feature_task.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAll(): Flow<List<Task>>
    suspend fun get(uid: Long): Task
    suspend fun insert(task: Task): Long
    suspend fun insertAll(tasks: Iterable<Task>)
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
    suspend fun deleteAll()
    suspend fun addSampleData()

}