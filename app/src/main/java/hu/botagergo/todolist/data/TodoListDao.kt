package hu.botagergo.todolist.data

import androidx.room.*
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task.data.model.TaskEnumPropertyValueEntity
import hu.botagergo.todolist.feature_task.data.model.PropertyEntity
import hu.botagergo.todolist.feature_task_view.data.model.PredicateEntity
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    // Tasks

    // Returns all tasks
    @Query("SELECT * FROM task ORDER BY uid")
    fun getTasks(): Flow<List<TaskEntity>>

    // Returns the task with the specified uid
    @Query("SELECT * FROM task WHERE uid=:uid ")
    suspend fun getTask(uid: Long): TaskEntity

    // Inserts the task
    // Throws exception if already exists
    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    // Deletes the task if exists
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    // Deletes all tasks
    @Query("DELETE FROM task")
    suspend fun deleteTasks()

    // Updates the task if exists
    @Update
    suspend fun updateTask(task: TaskEntity)

    // Task properties

    @Query("SELECT * FROM task_property WHERE id=:id")
    suspend fun getTaskProperty(id: String): PropertyEntity

    @Query("SELECT * FROM task_property")
    fun getTaskProperties(): Flow<List<PropertyEntity>>

    @Insert
    suspend fun insertTaskProperty(propertyEntity: PropertyEntity)

    // Task enum property values

    @Query("SELECT * FROM task_enum_property_value WHERE propertyId=:propertyId AND id=:enumValueId")
    suspend fun getTaskEnumPropertyValue(propertyId: String, enumValueId: Long): TaskEnumPropertyValueEntity

    @Query("SELECT * FROM task_enum_property_value WHERE propertyId=:propertyId")
    fun getTaskEnumPropertyValues(propertyId: String): Flow<List<TaskEnumPropertyValueEntity>>

    @Insert
    suspend fun insertTaskEnumPropertyValue(taskEnumPropertyValueEntity: TaskEnumPropertyValueEntity)

    // Task filters

    @Query("SELECT * FROM task_filter WHERE id=:id ")
    suspend fun getTaskFilter(id: Long): TaskFilterEntity

    @Query("SELECT * FROM task_filter")
    fun getTaskFilters(): Flow<List<TaskFilterEntity>>

    @Insert
    suspend fun insertTaskFilter(taskFilter: TaskFilterEntity)

    @Update
    suspend fun updateTaskFilter(taskFilter: TaskFilterEntity)

    // Task filter children

    @Query("SELECT * FROM task_filter JOIN task_filter_child ON task_filter.id=task_filter_child.childId")
    suspend fun getTaskFilterChildren(id: Long): List<TaskFilterEntity>

    // Predicates

    @Query("SELECT * FROM predicate WHERE id=:id")
    suspend fun getPredicate(id: Long): PredicateEntity

}