package hu.botagergo.todolist.sorter

import hu.botagergo.todolist.util.NamedByResource
import java.io.Serializable

interface SortSubject<T> : NamedByResource, Serializable {
    fun makeCriterion(order: SortCriterion.Order): SortCriterion<T>
}