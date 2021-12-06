package hu.botagergo.todolist.group

import kotlin.reflect.KProperty1

class PropertyGrouper<T, K>(
    private val property: KProperty1<T, K>,
    private val nameRes: Int,
    private val keyIfNull: K
) : GrouperBase<T, K>() {

    override fun key(item: T): K {
        return property.get(item) ?: keyIfNull
    }

    override fun clone(): Grouper<T, K> {
        return PropertyGrouper(property, nameRes, keyIfNull)
    }

    override fun getName(): Int = nameRes

}