package hu.botagergo.todolist.group

abstract class GrouperBase<K, T> : Grouper<K, T> {

    override fun group(items: List<T>, order: MutableList<K>?): MutableList<Pair<K, List<T>>> {
        val groupedItems = items.groupBy(::key).toList()
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

    abstract fun key(item: T): K

}