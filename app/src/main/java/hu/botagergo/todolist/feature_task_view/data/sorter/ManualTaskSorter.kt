package hu.botagergo.todolist.feature_task_view.data.sorter

import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import java.util.*

class ManualTaskSorter(var uids: ArrayList<Long> = ArrayList()) : Sorter<TaskEntity> {

    override fun sort(items: ArrayList<TaskEntity>) {
        var pos = 0
        val toRemove = ArrayList<Long>()

        for (uid in uids) {
            val ind = items.indexOfFirst { item -> item.uid == uid }
            if (ind == -1) {
                toRemove.add(uid)
            } else {
                if (pos < ind) {
                    items.add(pos, items.removeAt(ind))
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

    override fun clone(): Sorter<TaskEntity> {
        return ManualTaskSorter(uids)
    }
}