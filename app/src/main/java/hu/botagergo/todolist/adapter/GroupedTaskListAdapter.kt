package hu.botagergo.todolist.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.*
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import kotlin.collections.ArrayList

class GroupedTaskListAdapter(
    application: ToDoListApplication,
    tasks: ArrayList<Task>, taskListView: Configuration.TaskListView
) : Adapter(application, tasks, taskListView) {

    private lateinit var groupedTasks: MutableList<Pair<Any, List<Task>>>
    private var selectedItem: TaskItem? = null

    init {
        application.taskAddedEvent.subscribe {
            refresh()
        }

        application.taskRemovedEvent.subscribe {
            val item = getItemFromTask(it)
            item?.first?.remove(item.second)
        }

        application.taskChangedEvent.subscribe {
            refresh()
        }

        application.taskDataSetChangedEvent.subscribe {
            refresh()
        }

        refresh()
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
                this.add(ExpandableGroup(TaskGroupHeaderItem(taskGroup.first.toString())).apply {
                    this.add(Section().apply {
                        for (task in taskGroup.second) {
                            this.add(TaskItem(adapter, task))
                        }

                    })
                    this.isExpanded = true
                })
            }
        })
    }

    override fun refresh() {
        val sortedTasks = ArrayList<Task>().apply {
            addAll(tasks)
        }

        taskListView.filter.value?.apply(sortedTasks)
        taskListView.sorter.value?.sort(sortedTasks)
        groupedTasks = taskListView.grouper.value!!.group(sortedTasks)

        refreshItems()
    }

    private fun getItemFromTask(task: Task): Pair<Section, TaskItem>? {
        val section = this.getTopLevelGroup(0) as? Section ?: return null
        for (i in 0 until section.groupCount) {
            val group = section.getGroup(i) as? ExpandableGroup ?: return null
            val groupSection = group.getGroup(1) as? Section ?: return null
            for (j in 0 until groupSection.itemCount) {
                val item = groupSection.getItem(j) as? TaskItem ?: return null
                if (item.task.uid == task.uid) {
                    return Pair(groupSection, item)
                }
            }
        }
        return null
    }

    override fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(MyTouchCallback())
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

                        val sorter = adapter.taskListView.sorter.value
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