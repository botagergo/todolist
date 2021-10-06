package hu.botagergo.todolist.group

import kotlin.reflect.KProperty1

class PropertyGrouper<K, T>(private val property: KProperty1<T, K>) : GrouperBase<K, T>() {

    override fun key(item: T): K {
        return property.get(item)
    }

}