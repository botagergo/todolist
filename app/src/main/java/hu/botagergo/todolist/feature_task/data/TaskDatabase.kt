package hu.botagergo.todolist.feature_task.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 3, exportSchema = true)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}