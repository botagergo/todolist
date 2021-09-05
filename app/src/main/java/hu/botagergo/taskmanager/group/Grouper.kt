package hu.botagergo.taskmanager.group

import java.util.*

interface Grouper<K, T> {
    fun group(items: List<T>): SortedMap<K, List<T>>
}