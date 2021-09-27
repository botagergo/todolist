package hu.botagergo.todolist.group

import java.util.*

interface Grouper<K, T> {
    fun group(items: List<T>): MutableList<Pair<K, List<T>>>
}