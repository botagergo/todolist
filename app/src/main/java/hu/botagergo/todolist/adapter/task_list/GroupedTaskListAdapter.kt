package hu.botagergo.todolist.adapter.task_list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.*
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.log.loge
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskView
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import java.util.*
import kotlin.collections.ArrayList

class GroupedTaskListAdapter(
    application: ToDoListApplication,
    tasks: ArrayList<Task>, taskView: TaskView
) : Adapter(application, tasks, taskView) {

    private lateinit var groupedTasks: MutableList<Pair<Any?, List<Task>>>
    private var selectedItem: TaskItem? = null

    init {
        application.taskAddedEvent.subscribe {
            refresh()
        }

        application.taskRemovedEvent.subscribe {
            val item = getItemFromTask(it)
            if (item != null) {
                item.second.remove(item.third)

                if (item.second.groupCount == 0) {
                    section.remove(item.first)
                }
            }
        }

        application.taskChangedEvent.subscribe {
            refresh()
        }

        application.taskDataSetChangedEvent.subscribe {
            refresh()
        }

        refresh()
    }

    fun onGroupHeaderClicked(groupName: String, expanded: Boolean) {
        taskView.state.groupExpanded[groupName] = expanded
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

    private fun refreshItems() {
        val adapter = this
        this.clear()

        section = Section()

        this.add(section.apply {
            for (taskGroup in groupedTasks) {
                val groupName = taskGroup.first.toString()
                this.add(ExpandableGroup(TaskGroupHeaderItem(adapter, groupName)).apply {
                    this.add(Section().apply {
                        for (task in taskGroup.second) {
                            this.add(TaskItem(adapter, task))
                        }
                    })
                    this.isExpanded = taskView.state.groupExpanded[groupName] ?: true
                })
            }
        })
    }

    override fun refresh() {
        val sortedTasks = ArrayList<Task>().apply {
            addAll(tasks)
        }

        taskView.filter?.apply(sortedTasks)
        taskView.sorter?.sort(sortedTasks)
        groupedTasks = taskView.grouper!!.group(sortedTasks, taskView.state.groupOrder)

        refreshItems()
    }

    private fun getItemFromTask(task: Task): Triple<Group, Section, TaskItem>? {
        for (i in 0 until section.groupCount) {
            val group = section.getGroup(i) as? ExpandableGroup ?: return null
            val groupSection = group.getGroup(1) as? Section ?: return null
            for (j in 0 until groupSection.itemCount) {
                val item = groupSection.getItem(j) as? TaskItem ?: return null
                if (item.task.uid == task.uid) {
                    return Triple(group, groupSection, item)
                }
            }
        }
        return null
    }

    override fun getItemTouchHelper(): ItemTouchHelper? {
        return if (taskView.sorter is TaskReorderableSorter)
            ItemTouchHelper(MyTouchCallback())
        else
            null
    }

    inner class MyTouchCallback : TouchCallback() {
        override fun onMove(
            recyclerView: RecyclerView,
            source: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val adapter = this@GroupedTaskListAdapter
            val sourceItem = adapter.getItem(source.bindingAdapterPosition)
            val targetItem = adapter.getItem(target.bindingAdapterPosition)

            if (sourceItem is TaskGroupHeaderItem && targetItem is TaskGroupHeaderItem) {
                var toGroupIndex: Int = -1
                var fromGroup: Group? = null

                for ((i, group) in section.groups.withIndex()) {
                    if (group is ExpandableGroup) {
                        if (group.getItem(0) == sourceItem) {
                            fromGroup = group
                        } else if (group.getItem(0) == targetItem) {
                            toGroupIndex = i
                        }
                    }
                }

                if (toGroupIndex != -1 && fromGroup != null) {
                    val items = section.groups
                    items.remove(fromGroup)
                    items.add(toGroupIndex, fromGroup)
                    section.update(items)

                    val groupOrder = taskView.state.groupOrder
                    val ind1 = groupOrder.indexOfFirst {
                        it.toString() == sourceItem.groupName
                    }
                    val ind2 = groupOrder.indexOfFirst {
                        it.toString() == targetItem.groupName
                    }
                    if (ind1 != -1 && ind2 != -1) {
                        Collections.swap(groupOrder, ind1, ind2)
                    } else {
                        loge(this, "Group not found in group order list")
                    }

                    return true
                }
            } else if (sourceItem is TaskItem && targetItem is TaskItem) {
                var fromSection: Section? = null
                var toSection: Section? = null

                var targetIndex = -1

                for (group in adapter.section.groups) {
                    val expandableGroup = group as? ExpandableGroup ?: return false
                    val section = expandableGroup.getGroup(1) as? Section ?: return false

                    if (fromSection == null) {
                        val sourceIndex = section.groups.indexOf(sourceItem)
                        if (sourceIndex != -1) {
                            fromSection = section
                        }
                    }

                    if (toSection == null) {
                        targetIndex = section.groups.indexOf(targetItem)
                        if (targetIndex != -1) {
                            toSection = section
                        }
                    }
                }

                if (fromSection != null && toSection != null) {
                    if (fromSection == toSection) {
                        val items = fromSection.groups
                        items.remove(sourceItem)
                        items.add(targetIndex, sourceItem)
                        fromSection.update(items)

                        val sorter = adapter.taskView.sorter
                        if (sorter is TaskReorderableSorter) {
                            val toInd = sorter.taskUidList.indexOf(targetItem.task.uid)
                            sorter.taskUidList.remove(sourceItem.task.uid)
                            sorter.taskUidList.add(toInd, sourceItem.task.uid)
                        }

                        return true
                    }
                }
            }

            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            throw NotImplementedError()
        }
    }

}