package hu.botagergo.todolist.model

import androidx.room.*

@Dao
interface TaskDao {

    // Returns all tasks
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    // Returns the task with the specified uid
    @Query("SELECT * FROM task WHERE uid=:uid ")
    fun get(uid: Long): Task

    // Inserts the task
    // Throws exception if already exists
    @Insert
    fun insert(task: Task): Long

    // Deletes the task if exists
    @Delete
    fun delete(task: Task)

    // Deletes all tasks
    @Query("DELETE FROM task")
    fun deleteAll()

    // Updates the task if exists
    @Update
    fun update(task: Task)
}