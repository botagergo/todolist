package hu.botagergo.todolist.sorter

import hu.botagergo.todolist.util.Property
import java.io.Serializable

data class PropertySortCriterion<T>(
    val property: Property<T>,
    override val order: SortCriterion.Order = SortCriterion.Order.ASCENDING,
    val nullsLast: Boolean = true
) : SortCriterion<T>, Serializable {

    override val comparator: Comparator<T> = run {
        var comparator = Comparator.naturalOrder<Comparable<Any?>>()
        if (order == SortCriterion.Order.DESCENDING)
            comparator = comparator.reversed()
        comparator = if (nullsLast) nullsLast(comparator) else nullsFirst(comparator)
        Comparator.comparing({ property.getValue(it) }, comparator)

    }

}
