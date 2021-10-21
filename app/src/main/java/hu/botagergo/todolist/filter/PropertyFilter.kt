package hu.botagergo.todolist.filter

import kotlin.reflect.KProperty1

abstract class PropertyFilter<T, K>(private val property: KProperty1<T, K>) : Filter<T>() {

    protected fun getPropertyValue(t: T): K {
        return property.get(t)
    }

}