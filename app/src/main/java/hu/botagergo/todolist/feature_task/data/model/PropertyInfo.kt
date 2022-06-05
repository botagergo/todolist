package hu.botagergo.todolist.feature_task.data.model

import hu.botagergo.todolist.core.util.PropertyType

class PropertyInfo(
    val id: String,
    val resourceName: String,
    val displayName: String,
    val type: PropertyType
)