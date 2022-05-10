package hu.botagergo.todolist.group

import android.content.Context
import hu.botagergo.todolist.util.NamedByResource
import hu.botagergo.todolist.util.Property
import kotlin.reflect.KProperty1

class PropertyGrouper<T>(
    private val property: Property<T>,
    private val keyIfNull: Any
) : GrouperBase<T>() {

    override fun key(item: T, context: Context): Any {
        val value = property.getValue<Any?>(item) ?: return keyIfNull
        return if (value is NamedByResource) {
            context.getString(value.getName())
        } else {
            value.toString()
        }
    }

    override fun clone(): Grouper<T> {
        return PropertyGrouper(property, keyIfNull)
    }

    override fun getName(): Int = property.getName()

}