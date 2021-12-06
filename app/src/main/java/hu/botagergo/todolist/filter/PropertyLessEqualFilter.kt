package hu.botagergo.todolist.filter

import kotlin.reflect.KProperty1

class PropertyLessEqualFilter<T, K : Comparable<K>?> private constructor(
    property: KProperty1<T, K>,
    private val includeNull: Boolean = false
) : PropertyFilter<T, K>(property) {

    @Suppress("JoinDeclarationAndAssignment")
    private lateinit var getFilterValue: () -> K

    constructor(property: KProperty1<T, K>, value: () -> K, includeNull: Boolean = false) : this(
        property,
        includeNull
    ) {
        getFilterValue = value
    }

    override fun include(t: T): Boolean {
        return getPropertyValue(t)?.compareTo(getFilterValue()) ?: (if (includeNull) -1 else 1) <= 0
    }

    override fun clone(): Filter<T> {
        return PropertyLessEqualFilter(property, getFilterValue, includeNull)
    }

}