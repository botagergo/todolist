package hu.botagergo.todolist.feature_task.mapper

import android.content.Context
import hu.botagergo.todolist.core.util.EnumValue
import hu.botagergo.todolist.feature_task.data.model.TaskEnumPropertyValueEntity

fun TaskEnumPropertyValueEntity.toTaskEnumValue(context: Context): EnumValue {
    return EnumValue(
        id,
        if (this.valueType == TaskEnumPropertyValueEntity.ValueType.STRING)
            this.value
        else
            context.getString(
                context.resources.getIdentifier(
                    this.value,
            "string",
                    context.packageName
                )
            )
    )
}