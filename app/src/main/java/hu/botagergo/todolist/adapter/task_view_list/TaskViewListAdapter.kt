@file:Suppress("ConvertTwoComparisonsToRangeCheck")

package hu.botagergo.todolist.adapter.task_view_list

import android.app.Application
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
    val app: Application,
    val context: Context
) : GroupieAdapter() {

    private lateinit var section: Section

    lateinit var selectedViews: ArrayList<TaskView>
    lateinit var availableViews: ArrayList<TaskView>

    init {
        refresh()
    }

    fun refresh() {
        val adapter = this
        this.clear()

        selectedViews = ArrayList(config.selectedTaskViews.map {
            config.taskViews[it]!!
        })

        availableViews = ArrayList(config.taskViews.values)

        section = Section().apply {
            this.add(TaskViewHeaderItem(app.resources.getString(R.string.task_view_selected)))
            for (view in selectedViews) {
                this.add(TaskViewItem(adapter, view, context))
            }

            this.add(TaskViewHeaderItem(app.resources.getString(R.string.task_view_available)))
            for (view in availableViews) {
                this.add(TaskViewItem(adapter, view, context))
            }
        }

        this.add(section)
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(MyTouchCallback())
    }

    inner class MyTouchCallback : TouchCallback() {

        override fun onMove(
            recyclerView: RecyclerView,
            source: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            if (source.bindingAdapterPosition == -1 || target.bindingAdapterPosition == -1) {
                return false
            }
            val adapter = this@TaskViewListAdapter
            val sourceItem = adapter.getItem(source.bindingAdapterPosition)

            if (target.bindingAdapterPosition == 0 || sourceItem !is TaskViewItem) {
                return false
            }

            val sourceIndex = source.bindingAdapterPosition
            val targetIndex = target.bindingAdapterPosition

            if (sourceIndex == -1 || targetIndex == -1) {
                return false
            }

            val items = adapter.section.groups

            if (sourceIndex > selectedViews.size+1 && targetIndex <= selectedViews.size+1 && sourceItem.view !in selectedViews) {
                items.add(targetIndex, TaskViewItem(adapter, sourceItem.view, context))
                selectedViews.add(targetIndex - 1, sourceItem.view)
                config.selectedTaskViews.add(targetIndex - 1, sourceItem.view.uuid)
            } else if (sourceIndex < selectedViews.size+1 && targetIndex > selectedViews.size+1) {
                items.remove(sourceItem)
                source.itemView.visibility = View.GONE
                selectedViews.remove(sourceItem.view)
                config.selectedTaskViews.remove(sourceItem.view.uuid)
            } else if (sourceIndex < selectedViews.size+1 && targetIndex < selectedViews.size+1) {
                items.removeAt(sourceIndex)
                items.add(targetIndex, sourceItem)
                Collections.swap(selectedViews, sourceIndex-1, targetIndex-1)
                Collections.swap(config.selectedTaskViews, sourceIndex-1, targetIndex-1)
            }

            adapter.section.update(items)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            throw NotImplementedError()
        }
    }

}