package hu.botagergo.todolist.adapter

import android.app.Application
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import hu.botagergo.todolist.TaskView

class TaskViewListAdapter(val app: Application, val views: List<TaskView>, private val selected: Boolean) :
    GroupieAdapter() {

    private lateinit var section: Section

    init {
        refresh()
    }

    interface Listener {
        fun onItemClick(view: TaskView, selected: Boolean)
    }

    var listener: Listener? = null

    fun addView(view: TaskView) {
        section.add(TaskViewItem(this, view, selected))
    }

    private fun refresh() {
        val adapter = this
        this.clear()
        section = Section().apply {
            for (view in views) {
                this.add(TaskViewItem(adapter, view, selected))
            }
        }
        this.add(section)
    }

    private fun findGroup(item: TaskViewItem): Pair<Section, TaskViewItem>? {
        val group = getTopLevelGroup(0)
        for (j in 0 until group.itemCount) {
            val currItem = group.getItem(j) as? TaskViewItem
            if (currItem?.view == item.view) {
                return Pair(group as Section, item)
            }
        }
        return null
    }

    fun onItemClick(item: TaskViewItem) {
        val group = findGroup(item)
        group?.first?.remove(group.second)
        listener?.onItemClick(item.view, selected)
    }

}