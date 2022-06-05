package hu.botagergo.todolist.feature_task.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.botagergo.todolist.core.util.PropertyType

@Entity(
    tableName = "property"
)
class PropertyEntity(
    @PrimaryKey val id: String,
    val resourceName: String,
    val type: PropertyType
)