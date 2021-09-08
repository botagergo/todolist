package hu.botagergo.todolist.group

import java.util.*

abstract class GrouperBase<K, T>(var comp: Comparator<K>): Grouper<K, T> {

    override fun group(items: List<T>): SortedMap<K, List<T>> {
        return items.groupBy(::key).toSortedMap(comp)
    }

    abstract fun key(item: T): K

}