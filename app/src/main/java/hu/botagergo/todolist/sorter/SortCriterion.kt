package hu.botagergo.todolist.sorter

interface SortCriterion<T> {
    @Suppress("unused") enum class Order { ASCENDING, DESCENDING }
    fun compare(t1: T, t2: T): Int
    fun getOrder(): Order
    fun setOrder(order: Order)
    fun getSubject(): SortSubject<T>
    fun setSubject(subject: SortSubject<T>)
}