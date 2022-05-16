package hu.botagergo.todolist.sorter

import hu.botagergo.todolist.model.Task
import java.util.*

class ManualTaskSorter(var uids: ArrayList<Long> = ArrayList()) : Sorter<Task> {

    override fun sort(items: ArrayList<Task>) {
        var pos = 0
        val toRemove = ArrayList<Long>()

        for (uid in uids) {
            val ind = items.indexOfFirst { item -> item.uid == uid }
            if (ind == -1) {
                toRemove.add(uid)
            } else {
                if (pos < ind) {
                    Collections.swap(items, pos, ind)
                }
                pos++
            }
        }

        while (pos < items.size) {
            uids.add(items[pos].uid)
            pos += 1
        }

        uids.removeAll(toRemove.toSet())
    }

    override fun clone(): Sorter<Task> {
        return ManualTaskSorter(uids)
    }
}