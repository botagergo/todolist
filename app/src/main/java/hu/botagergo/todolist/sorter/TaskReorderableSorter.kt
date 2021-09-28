package hu.botagergo.todolist.sorter

import hu.botagergo.todolist.model.Task
import java.util.*
import kotlin.collections.ArrayList

class TaskReorderableSorter(val removeIfNotExists: Boolean = false) : Sorter<Task> {
    var taskUidList: ArrayList<Long> = ArrayList()

    override fun sort(items: ArrayList<Task>) {
        var pos = 0
        val toRemove = ArrayList<Int>()

        for (uid in taskUidList) {
            if (pos >= items.size) {
                break
            }

            val ind = items.indexOfFirst { item -> item.uid == uid }
            if (ind == -1) {
                if (removeIfNotExists) {
                    toRemove.add(ind)
                }
            } else {
                if (pos != ind) {
                    Collections.swap(items, pos, ind)
                }
                pos++
            }
        }

        while (pos < items.size) {
            taskUidList.add(items[pos].uid)
            pos++
        }

        for (ind in toRemove) {
            taskUidList.removeAt(ind)
        }
    }
}