package hu.botagergo.todolist.adapter.task_list

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemTaskBinding
import hu.botagergo.todolist.model.Task
import java.time.LocalDate

class TaskItem(private val adapter: TaskListAdapter, var task: Task) : BindableItem<ItemTaskBinding>() {

    var selected: Boolean = false

    val statusColor: Int
        get() {
            return ResourcesCompat.getColor(
                adapter.context.resources, when (task.status) {
                    Predefined.TaskStatusValues.nextAction -> R.color.status_next_action
                    Predefined.TaskStatusValues.waiting -> R.color.status_waiting
                    Predefined.TaskStatusValues.planning -> R.color.status_planning
                    Predefined.TaskStatusValues.onHold -> R.color.status_on_hold
                    else -> R.color.status_none
                }, null
            )
        }

    val statusString: String
        get() = if (task.status == null) "" else "@" + adapter.context.getString(task.status!!.value)

    val contextString: String
        get() = if (task.context == null) "" else "@" + adapter.context.getString(task.context!!.value)

    val doneIcon: Drawable?
        get() {
            return ResourcesCompat.getDrawable(
                adapter.context.resources,
                if (task.done) R.drawable.ic_undo else R.drawable.ic_done,
                null
            )
        }

    fun onItemClicked() {
        adapter.onItemSelected(this)
    }

    fun onDoneClicked() {
        adapter.onItemDoneClicked(this)
    }

    fun onEditClicked() {
        adapter.onItemClicked(this)
    }

    fun onDeleteClicked() {
        adapter.onItemDeleteClicked(this)
    }

    override fun getLayout() = R.layout.item_task
    override fun getDragDirs() = ItemTouchHelper.UP or ItemTouchHelper.DOWN

    override fun bind(viewBinding: ItemTaskBinding, position: Int) {
        viewBinding.data = this
    }

    fun getDueMessage(dueDate: LocalDate?): String {
        val today = LocalDate.now()
        return when {
            dueDate == null -> {
                ""
            }
            dueDate.dayOfYear < today.dayOfYear -> {
                "Overdue"
            }
            dueDate.dayOfYear == today.dayOfYear -> {
                "Due today"
            }
            dueDate.dayOfYear == today.dayOfYear+1 -> {
                "Due tomorrow"
            }
            else -> {
                "Due in ${(dueDate.dayOfYear - today.dayOfYear)} days"
            }
        }
    }

}