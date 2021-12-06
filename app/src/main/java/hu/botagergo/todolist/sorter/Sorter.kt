package hu.botagergo.todolist.sorter

import java.io.Serializable

interface Sorter<T> : Serializable, Cloneable {
    fun sort(items: ArrayList<T>)
    public override fun clone(): Sorter<T>
}