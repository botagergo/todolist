package hu.botagergo.todolist.group

import hu.botagergo.todolist.util.NamedByResource
import hu.botagergo.todolist.util.Property

class PropertyGrouper<T>(
    private val property: Property<T>,
    private val keyIfNull: Any
) : GrouperBase<T>() {

    override fun key(item: T): Any {
        val value = property.getValue<Any?>(item) ?: return keyIfNull
        return if (value is NamedByResource) {
            value.getName()
        } else {
            value.toString()
        }
    }

    override fun clone(): Grouper<T> {
        return PropertyGrouper(property, keyIfNull)
    }

    override fun getName(): Int = property.getName()

}