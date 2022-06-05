@file:Suppress("ConvertTwoComparisonsToRangeCheck")

package hu.botagergo.todolist.feature_task_view.presentation.task_view_list.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.*
import hu.botagergo.todolist.R
import hu.botagergo.todolist.feature_task_view.data.model.TaskView
import hu.botagergo.todolist.feature_task.domain.use_case.TaskViewUseCase
import java.util.*

class TaskViewListAdapter(
    val context: Context,
    private val taskViewUseCase: TaskViewUseCase
) : GroupieAdapter() {

    private val activeSection: Section = Section()
    private val allSection: Section = Section()

    private var activeViews: ArrayList<TaskView> = ArrayList()
    private var allViews: List<TaskView> = ArrayList()

    init {
        add(TaskViewHeaderItem(context.resources.getString(R.string.task_view_active)))
        add(activeSection)

        add(TaskViewHeaderItem(context.resources.getString(R.string.task_view_all)))
        add(allSection)
    }

    fun refresh() {
        activeSection.clear()
        allSection.clear()

        activeViews = ArrayList(taskViewUseCase.getActiveTaskViews())
        activeViews.forEach { taskView ->
            activeSection.add(TaskViewItem(this, taskView, true))
        }

        allViews = taskViewUseCase.getTaskViews()
        allViews.forEach { taskView ->
            allSection.add(TaskViewItem(this, taskView, false).apply {
                if (taskView in activeViews) {
                    buttonVisible.set(false)
                }
            })
        }
    }

    private fun addActiveView(view: TaskView) {
        activeSection.add(TaskViewItem(this, view, true))
        activeViews.add(view)
        taskViewUseCase.addActiveTaskView(view)

        val item = allSection.getItem(allViews.indexOf(view)) as TaskViewItem
        item.buttonVisible.set(false)
    }

    private fun removeActiveView(viewItem: TaskViewItem) {
        activeSection.remove(viewItem)
        activeViews.remove(viewItem.view)
        taskViewUseCase.deleteActiveTaskView(viewItem.view)

        val item = allSection.getItem(allViews.indexOf(viewItem.view)) as TaskViewItem
        item.buttonVisible.set(true)
    }

    fun onButtonClicked(viewItem: TaskViewItem) {
        if (viewItem.active) {
            removeActiveView(viewItem)
        } else {
            addActiveView(viewItem.view)
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
                    addActiveView(sourceItem.view)
                } else if (sourceIndex < activeViews.size+1 && targetIndex > activeViews.size+1) {
                    removeActiveView(sourceItem)
                    source.itemView.visibility = View.GONE
                } else if (sourceIndex < activeViews.size+1 && targetIndex < activeViews.size+1) {
                    val groups = activeSection.groups
                    groups.removeAt(sourceIndex-1)
                    groups.add(targetIndex-1, sourceItem)
                    Collections.swap(activeViews, sourceIndex-1, targetIndex-1)
                    taskViewUseCase.deleteActiveTaskView(sourceIndex-1)
                    taskViewUseCase.addActiveTaskView(targetIndex-1, activeViews[targetIndex-1])
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