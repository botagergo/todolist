package hu.botagergo.todolist.group

import java.util.*

abstract class GrouperBase<K, T>(var comp: Comparator<K>): Grouper<K, T> {

    override fun group(items: List<T>): MutableList<Pair<K, List<T>>> {
        return items.groupBy(::key).toSortedMap(comp).toList().toMutableList()
    }

    abstract fun key(item: T): K

}