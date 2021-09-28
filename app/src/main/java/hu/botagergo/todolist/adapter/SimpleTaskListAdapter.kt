package hu.botagergo.todolist.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.Section
import com.xwray.groupie.TouchCallback
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import kotlin.collections.ArrayList

class SimpleTaskListAdapter(
    application: ToDoListApplication,
    tasks: ArrayList<Task>, taskListView: Configuration.TaskListView
) : Adapter(application, tasks, taskListView) {

    private lateinit var displayedTasks: ArrayList<Task>
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

    override fun refresh() {
        val adapter = this

        this.clear()

        displayedTasks = ArrayList<Task>().apply {
            addAll(tasks)
        }
        taskListView.filter.value?.apply(displayedTasks)
        taskListView.sorter.value?.sort(displayedTasks)

        section = Section()

        this.add(section.apply {
            for (task in displayedTasks) {
                this.add(TaskItem(adapter, task))
            }
        })

    }

    private fun getItemFromTask(task: Task): Pair<Section, TaskItem>? {
        val group = getTopLevelGroup(0)
        for (j in 0 until group.itemCount) {
            val item = group.getItem(j) as? TaskItem
            if (item?.task?.uid == task.uid) {
                return Pair(group as Section, item)
            }
        }
        return null
    }

    override fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(MyTouchCallback())
    }

    inner class MyTouchCallback : TouchCallback() {
        override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            val adapter = this@SimpleTaskListAdapter

            val item = adapter.getItem(source.bindingAdapterPosition) as TaskItem
            val targetItem = adapter.getItem(target.bindingAdapterPosition) as TaskItem

            val section = adapter.section

            val items = section.groups
            val targetIndex = items.indexOf(targetItem)
            items.remove(item)
            items.add(targetIndex, item)
            section.update(items)

            val sorter = adapter.taskListView.sorter.value
            if (sorter is TaskReorderableSorter) {
                val toInd = sorter.taskUidList.indexOf(targetItem.task.uid)
                sorter.taskUidList.remove(item.task.uid)
                sorter.taskUidList.add(toInd, item.task.uid)
            }

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            throw NotImplementedError()
        }
    }

}