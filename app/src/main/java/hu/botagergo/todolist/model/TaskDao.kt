package hu.botagergo.todolist.model

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE uid=:uid ")
    fun get(uid: Long): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task): Long

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task")
    fun deleteAll()

    @Update
    fun update(task: Task)
}