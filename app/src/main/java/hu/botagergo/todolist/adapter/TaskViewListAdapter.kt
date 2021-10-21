package hu.botagergo.todolist.adapter

import android.app.Application
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.*
import hu.botagergo.todolist.R
import hu.botagergo.todolist.TaskView
import hu.botagergo.todolist.config
import hu.botagergo.todolist.log.logd
import java.util.*

class TaskViewListAdapter(
    val app: Application,
    var selectedViews: ArrayList<TaskView>,
    var availableViews: ArrayList<TaskView>
) :
    GroupieAdapter() {

    private lateinit var section: Section

    init {
        refresh()
    }

    private fun refresh() {
        val adapter = this
        this.clear()

        section = Section().apply {
            this.add(TaskViewHeaderItem(app.resources.getString(R.string.task_view_selected)))
            for (view in selectedViews) {
                this.add(TaskViewItem(adapter, view, true))
            }

            this.add(TaskViewHeaderItem(app.resources.getString(R.string.task_view_available)))
            for (view in availableViews) {
                this.add(TaskViewItem(adapter, view, false))
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
                items.add(targetIndex, TaskViewItem(adapter, sourceItem.view, false))
                selectedViews.add(targetIndex-1, sourceItem.view)
                config.selectedTaskViews.add(targetIndex-1, sourceItem.view.uuid)
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