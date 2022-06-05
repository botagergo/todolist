package hu.botagergo.todolist.feature_task.mapper

import android.content.Context
import hu.botagergo.todolist.core.util.*
import hu.botagergo.todolist.feature_task.data.model.PropertyInfo
import hu.botagergo.todolist.feature_task.data.model.PropertyEntity
import hu.botagergo.todolist.feature_task.data.model.Task
import kotlin.reflect.KProperty1


fun PropertyEntity.toTaskProperty(context: Context): PropertyInfo<Task> {

    val resourceId = context.resources.getIdentifier(
        this.resourceName,
        "string",
        context.packageName
    )

    val displayName = context.getString(resourceId)

    val property = Task::class.members.first {
        it.name == id
    } as KProperty1<Task, String?>

    when (type) {
        PropertyType.BOOLEAN -> {
            return BooleanProperty(id, resourceName, displayName, property)
        }
        PropertyType.STRING -> {
            return StringProperty(id, resourceName, displayName, property)
        }
        PropertyType.ENUM -> {
            return EnumProperty(id, resourceName, displayName, property)
        }
        PropertyType.DATE -> {
            return DateProperty(id, resourceName, displayName, property)
        }
        PropertyType.TIME -> {
            return TimeProperty(id, resourceName, displayName, property)
        }
    }
}

fun PropertyEntity.toPropertyInfo(context: Context): PropertyInfo {

    val resourceId = context.resources.getIdentifier(
        this.resourceName,
        "string",
        context.packageName
    )

    val displayName = context.getString(resourceId)

    return PropertyInfo(id, resourceName, displayName, type)
}


fun PropertyInfo.toTaskPropertyEntity(): PropertyEntity {
    return PropertyEntity(
        id,
        resourceName,
        type
    )
}