package hu.botagergo.todolist.filter

import kotlin.reflect.KProperty1

class PropertyInFilter<T, K>(property: KProperty1<T, K>, private val values: Set<K>) : PropertyFilter<T, K>(property) {

    override fun include(t: T): Boolean {
        return values.contains(getPropertyValue(t))
    }

}