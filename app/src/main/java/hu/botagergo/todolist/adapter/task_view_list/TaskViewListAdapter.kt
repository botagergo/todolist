@file:Suppress("ConvertTwoComparisonsToRangeCheck")

package hu.botagergo.todolist.adapter.task_view_list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.*
import hu.botagergo.todolist.R
import hu.botagergo.todolist.config
import hu.botagergo.todolist.model.TaskView
import java.util.*

class TaskViewListAdapter(
    val context: Context
) : GroupieAdapter() {

    private val activeSection: Section = Section()
    private val allSection: Section = Section()

    private val activeViews: ArrayList<TaskView> = ArrayList()
    private val allViews: ArrayList<TaskView> = ArrayList()

    init {
        add(TaskViewHeaderItem(context.resources.getString(R.string.task_view_active)))
        add(activeSection)

        add(TaskViewHeaderItem(context.resources.getString(R.string.task_view_all)))
        add(allSection)

        refresh()
    }

    fun refresh() {
        activeSection.clear()
        activeViews.clear()

        allSection.clear()
        allViews.clear()

        config.activeTaskViews.forEach { uid ->
            val taskView = config.taskViews[uid]
            if (taskView != null) {
                activeSection.add(TaskViewItem(this, taskView, true))
                activeViews.add(taskView)
            }
        }

        config.taskViews.values.forEach { taskView ->
            allSection.add(TaskViewItem(this, taskView, false).apply {
                if (taskView in activeViews) {
                    buttonVisible.set(false)
                }
            })
            allViews.add(taskView)
        }
    }

    private fun addActiveView(ind: Int, view: TaskView) {
        activeSection.add(ind, TaskViewItem(this, view, true))
        activeViews.add(view)
        config.activeTaskViews.add(view.uuid)

        val item = allSection.getItem(allViews.indexOf(view)) as TaskViewItem
        item.buttonVisible.set(false)
    }

    private fun removeActiveView(viewItem: TaskViewItem) {
        activeSection.remove(viewItem)
        activeViews.remove(viewItem.view)
        config.activeTaskViews.remove(viewItem.view.uuid)

        val item = allSection.getItem(allViews.indexOf(viewItem.view)) as TaskViewItem
        item.buttonVisible.set(true)
    }

    fun onButtonClicked(viewItem: TaskViewItem) {
        if (viewItem.active) {
            removeActiveView(viewItem)
        } else {
            addActiveView(activeViews.size, viewItem.view)
        }
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object: TouchCallback() {
            override fun onMove(
                recyclerView: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourceIndex = source.bindingAdapterPosition
                val targetIndex = target.bindingAdapterPosition

                if (sourceIndex == -1 || targetIndex == -1) {
                    return false
                }

                val sourceItem = this@TaskViewListAdapter.getItem(sourceIndex)

                if (sourceItem !is TaskViewItem) {
                    return false
                }

                if (sourceIndex > activeViews.size+1 && targetIndex <= activeViews.size+1 && sourceItem.view !in activeViews) {
                    addActiveView(activeViews.size, sourceItem.view)
                } else if (sourceIndex < activeViews.size+1 && targetIndex > activeViews.size+1) {
                    removeActiveView(sourceItem)
                    source.itemView.visibility = View.GONE
                } else if (sourceIndex < activeViews.size+1 && targetIndex < activeViews.size+1) {
                    val groups = activeSection.groups
                    groups.removeAt(sourceIndex-1)
                    groups.add(targetIndex-1, sourceItem)
                    Collections.swap(activeViews, sourceIndex-1, targetIndex-1)
                    Collections.swap(config.activeTaskViews, sourceIndex-1, targetIndex-1)
                    activeSection.update(groups)
                } else {
                    return false
                }

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                throw NotImplementedError()
            }
        })
    }

}