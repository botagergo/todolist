package hu.botagergo.todolist.group

import java.io.Serializable

interface Grouper<K, T> : Serializable {
    fun group(items: List<T>, order: MutableList<K>? = null): MutableList<Pair<K, List<T>>>
}