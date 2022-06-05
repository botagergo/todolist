package hu.botagergo.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task.data.model.PropertyEntity
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterChildEntity
import hu.botagergo.todolist.feature_task_view.data.model.TaskFilterEntity

@Database(
    entities = [
        TaskEntity::class,
        PropertyEntity::class,
        TaskFilterEntity::class,
        TaskFilterChildEntity::class
    ],
    version = 3, exportSchema = true)
abstract class TodoListDatabase : RoomDatabase() {
    abstract fun todoListDao(): TodoListDao
}