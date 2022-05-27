package hu.botagergo.todolist.feature_task_view.data.sorter

interface SortCriterion<T> {

    enum class Order { ASCENDING, DESCENDING }

    val comparator: Comparator<T>
    val order: Order

}