package hu.botagergo.todolist.adapter

import android.content.res.Resources
import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import hu.botagergo.todolist.R
import hu.botagergo.todolist.model.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskItem(private val adapter: Adapter, val task: Task) : Item() {

    var selected: Boolean = false

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.textView_title.text = task.title

        viewHolder.root.textView_status.text = task.status.value
        viewHolder.root.textView_status.setTextColor(
            getColorForStatus(viewHolder.root.textView_status.resources, task.status)
        )
        viewHolder.root.textView_context.text = if (task.context != Task.Context.None)
            task.context.value else ""

        viewHolder.root.textView_comments.text = task.comments
        viewHolder.root.textView_comments.visibility = if (selected) View.VISIBLE else View.GONE
        if (task.comments.isEmpty()) {
            viewHolder.root.textView_comments.visibility = View.GONE
        }

        viewHolder.root.cardView.background = getColor(
            viewHolder.root.cardView.resources,
            if (task.done) R.color.task_done_background else R.color.task_background
        ).toDrawable()

        if (selected) {
            viewHolder.root.constraintLayout_menu.visibility = View.VISIBLE

            viewHolder.root.imageButton_done.setImageResource(
                if (task.done) R.drawable.ic_undo else R.drawable.ic_done
            )

            viewHolder.root.imageButton_done.setOnClickListener {
                adapter.onItemDoneClicked(this)
            }

            viewHolder.root.imageButton_edit.setOnClickListener {
                adapter.onItemClicked(this)
            }

            viewHolder.root.imageButton_delete.setOnClickListener {
                adapter.onItemDeleteClicked(this)
            }
        } else {
            viewHolder.root.constraintLayout_menu.visibility = View.GONE
        }

        viewHolder.root.cardView.setOnClickListener {
            adapter.onItemSelected(this)
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_task
    }

    private fun getColor(resources: Resources, id: Int): Color {
        return Color.valueOf(ResourcesCompat.getColor(resources, id, null))
    }

    private fun getColorForStatus(resources: Resources, status: Task.Status): Int {
        return ResourcesCompat.getColor(
            resources, when (status) {
                Task.Status.NextAction -> R.color.status_next_action
                Task.Status.Waiting -> R.color.status_waiting
                Task.Status.Planning -> R.color.status_planning
                Task.Status.OnHold -> R.color.status_on_hold
                Task.Status.None -> R.color.status_none
            }, null
        )
    }

    override fun getDragDirs(): Int {
        return ItemTouchHelper.UP or ItemTouchHelper.DOWN
    }

}