package hu.botagergo.todolist.sorter

interface Sorter<T> {
    fun sort(items: ArrayList<T>)
}