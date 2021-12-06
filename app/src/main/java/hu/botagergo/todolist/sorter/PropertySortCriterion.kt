package hu.botagergo.todolist.sorter

import java.io.Serializable
import java.lang.IllegalArgumentException
import kotlin.Comparator

data class PropertySortCriterion<T, K: Comparable<K>?> (
    private var subject: PropertySortSubject<T, K>,
    private var order: SortCriterion.Order = SortCriterion.Order.ASCENDING,
    private var putNullsLast: Boolean = true
) : SortCriterion<T>, Serializable {

    @Transient
    private var comp: Comparator<K?> = if (putNullsLast) nullsLast(Comparator.comparing{it}) else nullsFirst(Comparator.comparing{it})

    private val orderSign: Int = if (order == SortCriterion.Order.ASCENDING) 1 else -1

    override fun compare(t1: T, t2: T) : Int {
        val p1 = subject.get(t1)
        val p2 = subject.get(t2)
        return comp.compare(p1, p2) * orderSign
    }

    override fun getOrder(): SortCriterion.Order = order
    override fun setOrder(order: SortCriterion.Order) {
        this.order = order
    }

    override fun getSubject(): SortSubject<T> = subject
    override fun setSubject(subject: SortSubject<T>) {
        if (subject !is PropertySortSubject<*, *>) {
            throw IllegalArgumentException()
        }
        this.subject = subject as PropertySortSubject<T, K>
    }

}