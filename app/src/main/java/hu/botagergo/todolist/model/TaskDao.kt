package hu.botagergo.todolist.model

import androidx.room.*

@Dao
interface TaskDao {

    // Returns all tasks
    @Query("SELECT * FROM task ORDER BY uid")
    suspend fun getAll(): List<Task>

    // Returns the task with the specified uid
    @Query("SELECT * FROM task WHERE uid=:uid ")
    suspend fun get(uid: Long): Task

    // Inserts the task
    // Throws exception if already exists
    @Insert
    suspend fun insert(task: Task): Long

    // Deletes the task if exists
    @Delete
    suspend fun delete(task: Task)

    // Deletes all tasks
    @Query("DELETE FROM task")
    suspend fun deleteAll()

    // Updates the task if exists
    @Update
    suspend fun update(task: Task)
}