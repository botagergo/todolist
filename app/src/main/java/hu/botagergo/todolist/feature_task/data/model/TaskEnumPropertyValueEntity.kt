package hu.botagergo.todolist.feature_task.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import hu.botagergo.todolist.Predefined

@Entity(
    tableName = "task_enum_property_value",
    foreignKeys = [ForeignKey(
        entity = Predefined.TaskProperty::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("propertyId"),
        onDelete = ForeignKey.CASCADE
    )]
)
class TaskEnumPropertyValueEntity(
    val propertyId: String,
    val valueType: ValueType,
    val value: String,
    @PrimaryKey val id: Int = 0
    ) {

    enum class ValueType {
        RESOURCE,
        STRING
    }

}