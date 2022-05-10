package hu.botagergo.todolist.group

import android.content.Context

abstract class GrouperBase<T> : Grouper<T> {

    override fun group(items: List<T>, context: Context, order: MutableList<Any?>?): MutableList<Pair<Any?, List<T>>> {
        val groupedItems = items.groupBy {t -> key(t, context)}.toList()
        return if (order != null) {
            var pos = order.size
            groupedItems.sortedBy {
                var ind = order.indexOf(it.first)
                if (ind == -1) {
                    ind = pos++
                    order.add(it.first)
                }
                ind
            }.toMutableList()
        } else {
            groupedItems.toMutableList()
        }
    }

    abstract fun key(item: T, context: Context): Any

}