package hu.botagergo.todolist.sorter

import java.io.Serializable

interface Sorter<T> : Serializable{
    fun sort(items: ArrayList<T>)
}