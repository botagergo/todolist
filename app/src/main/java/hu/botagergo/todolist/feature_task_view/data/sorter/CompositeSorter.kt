package hu.botagergo.todolist.feature_task_view.data.sorter

import java.lang.IllegalArgumentException

class CompositeSorter<T>(
    val sortCriteria: MutableList<SortCriterion<T>>
) : SorterBase<T> {

    @Transient
    private val comparator: Comparator<T> = run {
        if (sortCriteria.isEmpty()) {
            throw IllegalArgumentException("At least one sort criterion has to be defined")
        }
        val iter = sortCriteria.iterator()
        var comp = iter.next().comparator
        while (iter.hasNext()) {
            comp = comp.then(iter.next().comparator)
        }
        comp
    }

    override fun compare(t1: T, t2: T): Int {
        return comparator.compare(t1, t2)
    }

    override fun clone(): Sorter<T> {
        return CompositeSorter(sortCriteria)
    }

}