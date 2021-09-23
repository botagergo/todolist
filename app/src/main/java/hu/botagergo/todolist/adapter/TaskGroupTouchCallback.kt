package hu.botagergo.todolist.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import java.util.*

class TaskGroupTouchCallback(val adapter: TaskGroupListAdapter) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
) {

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val viewHolderFrom = source as TaskGroupListAdapter.TaskGroupViewHolder
        val viewHolderTo = target as TaskGroupListAdapter.TaskGroupViewHolder

        val indFrom = viewHolderFrom.bindingAdapterPosition
        val indTo = viewHolderTo.bindingAdapterPosition

        Collections.swap(adapter.taskGroups, indFrom, indTo)
        adapter.notifyItemMoved(indFrom, indTo)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        throw NotImplementedError()
    }

}
