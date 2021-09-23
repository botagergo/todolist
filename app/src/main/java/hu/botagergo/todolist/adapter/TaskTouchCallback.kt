package hu.botagergo.todolist.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import java.util.*

class TaskTouchCallback(val adapter: TaskListAdapter) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
) {
    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val sorter = adapter.taskListView.sorter.value as TaskReorderableSorter
        val viewHolderFrom = source as TaskListAdapter.TaskViewHolder
        val viewHolderTo = target as TaskListAdapter.TaskViewHolder

        val indFrom = sorter.taskUidList.indexOf(viewHolderFrom.task.uid)
        val indTo = sorter.taskUidList.indexOf(viewHolderTo.task.uid)
        Collections.swap(sorter.taskUidList, indFrom, indTo)
        Collections.swap(adapter.sortedTasks, viewHolderFrom.bindingAdapterPosition, viewHolderTo.bindingAdapterPosition)
        if (adapter.selectedTaskIndex == indFrom) {
            adapter.selectedTaskIndex = indTo
        }
        adapter.notifyItemMoved(viewHolderFrom.bindingAdapterPosition, viewHolderTo.bindingAdapterPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        throw NotImplementedError()
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = if (adapter.taskListView.sorter.value is TaskReorderableSorter) {
            ItemTouchHelper.UP or ItemTouchHelper.DOWN
        } else 0

        return makeMovementFlags(dragFlags, 0)
    }
}
