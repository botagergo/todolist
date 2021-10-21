package hu.botagergo.todolist.filter

import kotlin.reflect.KProperty1

class PropertyLessEqualFilter<T, K: Comparable<K>?> private constructor(property: KProperty1<T, K>, includeNull: Boolean=false) : PropertyFilter<T, K>(property) {

    private lateinit var getFilterValue: () -> K

    constructor(property: KProperty1<T, K>, value: K, includeNull: Boolean=false) : this(property, includeNull) {
        getFilterValue = { value }
    }

    constructor(property: KProperty1<T, K>, value: () -> K, includeNull: Boolean=false) : this(property, includeNull) {
        getFilterValue = value
    }

    override fun include(t: T): Boolean {
        return getPropertyValue(t)?.compareTo(getFilterValue()) ?: 1 <= 0
    }

}