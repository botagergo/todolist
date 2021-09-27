package hu.botagergo.todolist.adapter

import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Group
import com.xwray.groupie.Section
import com.xwray.groupie.TouchCallback
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import java.util.*
import kotlin.collections.ArrayList

class GroupedTaskListAdapter(
    application: ToDoListApplication,
    tasks: ArrayList<Task>, taskListView: Configuration.TaskListView
) : Adapter(application, tasks, taskListView) {

    private lateinit var groupedTasks: MutableList<Pair<Any, List<Task>>>
    private var selectedItem: TaskItem? = null

    private var thisAdapter = this

    init {
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
        this.clear()
        section = Section()

        val adapter = this

        for (taskGroup in groupedTasks) {
            section.add(ExpandableGroup(TaskGroupHeaderItem(taskGroup.first.toString())).apply {
                this.add(Section().apply {
                    for (task in taskGroup.second) {
                        this.add(TaskItem(adapter, task))
                    }

                })
                this.isExpanded = true
            })
        }

        this.add(section)
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

    private fun getItemFromTask(task: Task): Pair<ExpandableGroup, TaskItem>? {

        for (i in 0 until groupCount) {
            val group = getTopLevelGroup(i)
            for (j in 0 until group.itemCount) {
                val item = group.getItem(j) as? TaskItem
                if (item?.task?.uid == task.uid) {
                    return Pair(group as ExpandableGroup, item)
                }
            }
        }
        return null
    }

    override fun getTouchCallback() = object : TouchCallback() {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {

            val sourceItem = thisAdapter.getItem(viewHolder.adapterPosition)
            val targetItem = thisAdapter.getItem(target.adapterPosition)

            if (sourceItem is TaskGroupHeaderItem && targetItem is TaskGroupHeaderItem) {
                var fromGroupIndex: Int = -1
                var toGroupIndex: Int = -1

                for ((i, group) in section.groups.withIndex()) {
                    if (group is ExpandableGroup){
                        if (group.getItem(0) == sourceItem) {
                            fromGroupIndex = i
                        } else if (group.getItem(0) == targetItem) {
                            toGroupIndex = i
                        }
                    }
                }

                val group = thisAdapter.groupedTasks.removeAt(fromGroupIndex)
                thisAdapter.groupedTasks.add(toGroupIndex, group)

                thisAdapter.refreshItems()
            } else if (sourceItem is TaskItem && targetItem is TaskItem) {
                var fromGroup: Group? = null
                var toGroup: Group? = null

                for (group in thisAdapter.section.groups) {
                    val expandableGroup = group as ExpandableGroup
                    val section = expandableGroup.getGroup(1) as Section

                    val items = section.groups
                    val sourceIndex = items.indexOf(sourceItem)
                    val targetIndex = items.indexOf(targetItem)

                    if (sourceIndex != -1 && targetIndex != -1) {
                        items.remove(sourceItem)
                        items.add(targetIndex, sourceItem)
                        section.update(items)

                        val sorter = thisAdapter.taskListView.sorter.value
                        if (sorter is TaskReorderableSorter) {
                            val toInd = sorter.taskUidList.indexOf(targetItem.task.uid)
                            sorter.taskUidList.remove(sourceItem.task.uid)
                            sorter.taskUidList.add(toInd, sourceItem.task.uid)
                        }

                        break
                    }
                }
            }

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            throw NotImplementedError()
        }
    }
}