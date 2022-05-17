package hu.botagergo.todolist.adapter.task_list

import android.content.Context
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.model.Task
import java.util.*

class SimpleTaskListAdapter(
    context: Context
) : TaskListAdapter(context) {

    private var selectedItem: TaskItem? = null

    override var tasks: MutableList<Grouper.Group<Task>>?
        get() = tasksData
        set(value) {
            if (value == null || value.size == 0) {
                tasksData.clear()
                refresh()
            } else if (tasksData.size == 0) {
                tasksData.addAll(value)
                refresh()
            } else {
                updateTasks(rootSection, tasksData[0].items, value[0].items)
            }
        }

    override fun onItemSelected(taskItem: TaskItem) {
        taskItem.selected = !taskItem.selected
        if (taskItem.selected) {
            selectedItem?.selected = false
            selectedItem?.notifyChanged()
            selectedItem = taskItem
        } else {
            selectedItem = null
        }
        taskItem.notifyChanged()
    }

    override fun refresh() {
        rootSection.clear()
        if (tasksData.size > 0) {
            for (task in tasksData[0].items) {
                rootSection.add(TaskItem(this, task))
            }
        }
    }

    override fun moveItem(fromInd: Int, toInd: Int) {
        val item = getItem(fromInd) as? TaskItem ?: return
        val targetItem = getItem(toInd) as? TaskItem ?: return

        val items = rootSection.groups
        val targetIndex = items.indexOf(targetItem)
        items.remove(item)
        items.add(targetIndex, item)
        rootSection.update(items)

        Collections.swap(tasksData[0].items, fromInd, toInd)
    }

}