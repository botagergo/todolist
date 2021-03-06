package hu.botagergo.todolist.feature_task_view.data.sorter

interface SorterBase<T> : Sorter<T> {

    fun compare(t1: T, t2: T): Int

    override fun sort(items: ArrayList<T>) {
        items.sortWith { t1, t2 -> compare(t1, t2) }
    }

}