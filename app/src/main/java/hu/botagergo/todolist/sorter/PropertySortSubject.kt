package hu.botagergo.todolist.sorter

import kotlin.reflect.KProperty1

class PropertySortSubject<T, K : Comparable<K>?>(
    private val property: KProperty1<T, K>, private val nameRes: Int
) : SortSubject<T> {

    fun get(t: T): K = property.get(t)

    override fun getName(): Int = nameRes

    override fun makeCriterion(order: SortCriterion.Order): SortCriterion<T> {
        return PropertySortCriterion(this, order)
    }

}