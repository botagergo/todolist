package hu.botagergo.todolist.sorter

import java.lang.IllegalArgumentException

class SimpleSorter<T>(
    val sortCriteria: MutableList<SortCriterion<T>>
) : SorterBase<T> {

    init {
        if (sortCriteria.isEmpty()) {
            throw IllegalArgumentException("At least one comparer has to be defined")
        }
    }

    override fun compare(t1: T, t2: T): Int {
        var c: Int = -1
        for (sortCriterion in sortCriteria) {
            c = sortCriterion.compare(t1, t2)
            if (c != 0) {
                break
            }
        }
        return c
    }

    override fun clone(): Sorter<T> {
        return SimpleSorter(sortCriteria)
    }

}