package hu.botagergo.todolist.filter

import kotlin.reflect.KProperty1

class PropertyEqualsFilter<T, K>(property: KProperty1<T, K>, private val value: K) : PropertyFilter<T, K>(property) {

    override fun include(t: T): Boolean {
        return getPropertyValue(t) == value
    }

    override fun clone(): Filter<T> {
        return PropertyEqualsFilter(property, value)
    }

}