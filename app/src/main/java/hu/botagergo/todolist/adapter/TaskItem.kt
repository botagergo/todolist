package hu.botagergo.todolist.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemTaskBinding
import hu.botagergo.todolist.model.Task
import java.time.LocalDate

class TaskItem(private val adapter: Adapter, val task: Task) : BindableItem<ItemTaskBinding>() {

    var selected: Boolean = false


    val statusColor: Int
        get() {
            return ResourcesCompat.getColor(
                adapter.application.resources, when (task.status) {
                    Task.Status.NextAction -> R.color.status_next_action
                    Task.Status.Waiting -> R.color.status_waiting
                    Task.Status.Planning -> R.color.status_planning
                    Task.Status.OnHold -> R.color.status_on_hold
                    Task.Status.None -> R.color.status_none
                }, null
            )
        }

    val background: Drawable
        get() {
            val id = if (task.done) R.color.task_done_background else R.color.task_background
            return Color.valueOf(ResourcesCompat.getColor(adapter.application.resources, id, null))
                .toDrawable()
        }

    val doneIcon: Drawable?
        get() {
            return ResourcesCompat.getDrawable(
                adapter.application.resources,
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