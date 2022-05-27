package hu.botagergo.todolist.feature_task_view.data.group

abstract class GrouperBase<T> : Grouper<T> {

    override fun group(items: MutableList<T>, order: MutableList<Any>?): List<Grouper.Group<T>> {
        val groups = items.groupBy { t -> key(t) }.map { entry ->
            Grouper.Group(entry.key, entry.value.toMutableList())
        }.toMutableList()

        return if (order != null) {
            val finalGroups = mutableListOf<Grouper.Group<T>>()
            for (groupName in order) {
                val ind = groups.indexOfFirst { it.name == groupName }
                if (ind != -1) {
                    finalGroups.add(groups[ind])
                    groups.removeAt(ind)
                }
            }

            for (group in groups) {
                finalGroups.add(group)
            }

            finalGroups
        } else {
            groups
        }
    }

}