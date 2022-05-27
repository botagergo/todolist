package hu.botagergo.todolist.feature_task_view.data.sorter

import java.io.Serializable

interface Sorter<T> : Serializable, Cloneable {
    fun sort(items: ArrayList<T>)
    public override fun clone(): Sorter<T>
}