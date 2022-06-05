package hu.botagergo.todolist.feature_task.presentation.task_list.adapter

import android.content.Context
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Group
import com.xwray.groupie.Section
import hu.botagergo.todolist.feature_task_view.data.group.Grouper
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import java.util.*

class GroupedTaskListAdapter(
    context: Context,
    private val groupExpanded: MutableMap<Any, Boolean>
) : TaskListAdapter(context) {

    private var selectedItem: TaskItem? = null

    override var tasks: MutableList<Grouper.Group<TaskEntity>>?
        get() = tasksData
        set(value) {
            if (value == null || value.size == 0) {
                tasksData.clear()
                refresh()
            } else if (tasksData.size == 0) {
                tasksData.addAll(value)
                refresh()
            } else {
                updateTaskGroups(rootSection, tasksData, value)
            }
        }

    private fun updateTaskGroups(
        section: Section,
        taskGroups: MutableList<Grouper.Group<TaskEntity>>,
        newTaskGroups: MutableList<Grouper.Group<TaskEntity>>
    ) {
        var taskGroupInd = 0
        var newTaskGroupInd = 0

        while (taskGroupInd < taskGroups.size && newTaskGroupInd < newTaskGroups.size) {
            if (taskGroups[taskGroupInd].name == newTaskGroups[newTaskGroupInd].name) {
                updateTasks(
                    (section.getGroup(taskGroupInd) as ExpandableGroup).getGroup(1) as Section,
                    taskGroups[taskGroupInd].items, newTaskGroups[newTaskGroupInd].items
                )
                taskGroupInd++; newTaskGroupInd++
            } else {
                val groups = section.groups

                if (newTaskGroups.find { it.name == taskGroups[taskGroupInd].name } == null) {
                    taskGroups.removeAt(taskGroupInd)
                    groups.removeAt(taskGroupInd)
                    section.update(groups)
                } else {
                    val ind = taskGroups.indexOfLast {
                        it.name == newTaskGroups[newTaskGroupInd].name
                    }
                    if (ind != -1) {
                        if (ind <= taskGroupInd) {
                            throw RuntimeException()
                        }
                        taskGroups.removeAt(ind)
                        groups.removeAt(ind)
                    }
                    taskGroups.add(taskGroupInd, newTaskGroups[newTaskGroupInd])
                    groups.add(
                        taskGroupInd,
                        ExpandableGroup(
                            TaskGroupHeaderItem(
                                this,
                                newTaskGroups[newTaskGroupInd].name
                            )
                        ).apply {
                            add(Section().apply {
                                for (task in newTaskGroups[newTaskGroupInd].items) {
                                    add(TaskItem(this@GroupedTaskListAdapter, task))
                                }
                            })
                            isExpanded = groupExpanded[newTaskGroups[newTaskGroupInd].name] ?: true
                        })
                    section.update(groups)

                    taskGroupInd++; newTaskGroupInd++
                }
            }
        }

        while (taskGroupInd < taskGroups.size) {
            val groups = section.groups
            taskGroups.removeAt(taskGroupInd)
            groups.removeAt(taskGroupInd)
            section.update(groups)
        }

        while (newTaskGroupInd < newTaskGroups.size) {
            taskGroups.add(taskGroupInd, newTaskGroups[newTaskGroupInd])
            val groups = section.groups
            groups.add(
                taskGroupInd,
                ExpandableGroup(
                    TaskGroupHeaderItem(
                        this,
                        newTaskGroups[newTaskGroupInd].name
                    )
                ).apply {
                    add(Section().apply {
                        for (task in newTaskGroups[newTaskGroupInd].items) {
                            add(TaskItem(this@GroupedTaskListAdapter, task))
                        }
                    })
                    isExpanded = groupExpanded[newTaskGroups[newTaskGroupInd].name] ?: true
                })
            section.update(groups)
            taskGroupInd++; newTaskGroupInd++
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
        for (taskGroup in tasksData) {
            rootSection.add(
                ExpandableGroup(
                    TaskGroupHeaderItem(
                        this@GroupedTaskListAdapter,
                        taskGroup.name
                    )
                ).apply {
                    add(Section().apply {
                        for (task in taskGroup.items) {
                            add(TaskItem(this@GroupedTaskListAdapter, task))
                        }
                    })
                    isExpanded = groupExpanded[taskGroup.name] ?: true
                })
        }
    }

    override fun moveItem(fromInd: Int, toInd: Int) {
        val sourceItem = getItem(fromInd)
        val targetItem = getItem(toInd)

        if (sourceItem is TaskGroupHeaderItem && targetItem is TaskGroupHeaderItem) {
            var fromGroupIndex: Int = -1
            var toGroupIndex: Int = -1
            var fromGroup: Group? = null

            for ((i, group) in rootSection.groups.withIndex()) {
                if (group is ExpandableGroup) {
                    if (group.getItem(0) == sourceItem) {
                        fromGroup = group
                        fromGroupIndex = i
                    } else if (group.getItem(0) == targetItem) {
                        toGroupIndex = i
                    }
                }
            }

            if (toGroupIndex != -1 && fromGroup != null) {
                val items = rootSection.groups
                items.remove(fromGroup)
                items.add(toGroupIndex, fromGroup)
                rootSection.update(items)
                Collections.swap(tasksData, fromGroupIndex, toGroupIndex)
            }
        } else if (sourceItem is TaskItem && targetItem is TaskItem) {
            var fromSection: Section? = null
            var toSection: Section? = null

            var sourceIndex = -1
            var targetIndex = -1

            var groupInd = -1

            for ((currGroupInd, group) in rootSection.groups.withIndex()) {
                val expandableGroup = group as? ExpandableGroup ?: return
                val section = expandableGroup.getGroup(1) as? Section ?: return

                if (fromSection == null) {
                    sourceIndex = section.groups.indexOf(sourceItem)
                    if (sourceIndex != -1) {
                        fromSection = section
                        groupInd = currGroupInd
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
                    Collections.swap(tasksData[groupInd].items, sourceIndex, targetIndex)
                }
            }
        }
    }

    fun onGroupExpandedChanged(groupName: Any, expanded: Boolean) {
        groupExpanded[groupName] = expanded
    }

}