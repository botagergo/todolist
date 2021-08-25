package hu.botagergo.taskmanager

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task")
    fun deleteAll()

    @Update
    fun update(task: Task)
}