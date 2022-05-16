package hu.botagergo.todolist.group

abstract class GrouperBase<T> : Grouper<T> {

    override fun group(items: MutableList<T>, order: MutableList<Any?>?): List<Grouper.Group<T>> {
        val groups = items.groupBy { t -> key(t) }.map { entry ->
            Grouper.Group(entry.key, entry.value.toMutableList())
        }.sortedBy { group -> group.name as Comparable<Any> }

        return if (order != null) {
            var pos = order.size
            groups.sortedBy { group ->
                var ind = order.indexOf(group.name)
                if (ind == -1) {
                    ind = pos++
                    order.add(group.name)
                }
                ind
            }.toMutableList()
        } else {
            groups
        }

    }

}