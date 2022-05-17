package hu.botagergo.todolist.group

import hu.botagergo.todolist.util.NamedByResource
import hu.botagergo.todolist.util.Property
import java.lang.RuntimeException

class PropertyGrouper<T>(
    private val property: Property<T>,
    private val keyIfNull: Any? = null
) : GrouperBase<T>() {

    override fun key(item: T): Any {
        val value = property.getValue<Any?>(item) ?: return keyIfNull ?: throw RuntimeException()
        return if (value is NamedByResource) value.name else value.toString()
    }

    override fun clone(): Grouper<T> {
        return PropertyGrouper(property, keyIfNull)
    }

    override val name: Int = property.name

}