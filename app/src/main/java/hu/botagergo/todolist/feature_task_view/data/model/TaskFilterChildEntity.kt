package hu.botagergo.todolist.feature_task_view.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "task_filter_child",
    foreignKeys = [ForeignKey(
        entity = TaskFilterEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("childId"),
        onDelete = ForeignKey.CASCADE
    )]
)
class TaskFilterChildEntity(
    val id: Long,
    val childId: Long,
) {
}