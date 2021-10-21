package hu.botagergo.todolist.group

import kotlin.reflect.KProperty1

class PropertyGrouper<T, K>(private val property: KProperty1<T, K>) : GrouperBase<T, K>() {

    override fun key(item: T): K {
        return property.get(item)
    }

}