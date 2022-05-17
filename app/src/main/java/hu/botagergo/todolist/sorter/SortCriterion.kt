package hu.botagergo.todolist.sorter

interface SortCriterion<T> {

    enum class Order { ASCENDING, DESCENDING }

    val comparator: Comparator<T>
    val order: Order

}