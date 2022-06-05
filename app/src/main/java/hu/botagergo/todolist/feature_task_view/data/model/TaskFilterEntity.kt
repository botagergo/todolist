package hu.botagergo.todolist.feature_task_view.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.botagergo.todolist.core.util.FilterKind
import hu.botagergo.todolist.feature_task.data.model.PropertyEntity

@Entity(
    tableName = "task_filter",
    foreignKeys = [
        ForeignKey(
            entity = PropertyEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("propertyId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PredicateEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("predicateId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskFilterEntity (
    val kind: FilterKind,
    val propertyId: String?,
    val predicateId: Long?,
    val operand: String,
    @Relation(parentColumn = "id", entityColumn = "parentId")
    val children: List<TaskFilterEntity>,
    val parentId: Long,
    @PrimaryKey(autoGenerate = true) val id: Long
)