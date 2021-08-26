package hu.botagergo.taskmanager

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView

class TaskArrayAdapter(private var tasks: ArrayList<Task>, private var activity: Activity)
    : RecyclerView.Adapter<TaskArrayAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView_title)
        val textViewStatus: TextView = itemView.findViewById(R.id.textView_status)
        val textViewComments: TextView = itemView.findViewById(R.id.textView_comments)
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    var listener: Listener? = null

    fun setTasks(tasks: ArrayList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTask = inflater.inflate(R.layout.item_task, parent, false)

        val holder = ViewHolder(itemTask)

        val cardView = itemTask.findViewById<CardView>(R.id.cardView)
        val imageButton = itemTask.findViewById<ImageButton>(R.id.imageButton)

        cardView.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onTaskClicked(tasks[pos])
            }
        }

        cardView.setOnLongClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onTaskLongClicked(it, tasks[pos])
                true
            } else {
                false
            }
        }

        imageButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onDoneClicked(tasks[pos], !tasks[pos].done)
            }
        }

        return holder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val task: Task = tasks[position]

        viewHolder.textView.text = task.title
        viewHolder.textViewStatus.text = task.status.value
        viewHolder.textViewComments.text = task.comments

        val color = ResourcesCompat.getColor(viewHolder.textViewStatus.resources, when (task.status) {
            Task.Status.NextAction -> R.color.status_next_action
            Task.Status.Waiting -> R.color.status_waiting
            Task.Status.Planning -> R.color.status_planning
            Task.Status.OnHold -> R.color.status_on_hold
            Task.Status.None -> R.color.status_none
        }, null)

        viewHolder.textViewStatus.setTextColor(color)

        viewHolder.textViewComments.visibility = if (task.comments.isEmpty()) View.GONE else View.VISIBLE

        if (task.done) {
            viewHolder.cardView.background = getColor(R.color.task_done_background).toDrawable()
            viewHolder.imageButton.background = getColor(R.color.task_done_background).toDrawable()
            viewHolder.imageButton.setImageDrawable(getDrawable(R.drawable.ic_check_circle))
        }
        else {
            viewHolder.cardView.background = getColor(R.color.task_background).toDrawable()
            viewHolder.imageButton.background = getColor(R.color.task_background).toDrawable()
            viewHolder.imageButton.setImageDrawable(getDrawable(R.drawable.ic_circle))
        }
    }

    private fun getDrawable(id: Int): Drawable? {
        return ResourcesCompat.getDrawable(activity.resources, id, null)
    }

    private fun getColor(id: Int): Color {
        return Color.valueOf(ResourcesCompat.getColor(activity.resources, id, null))
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    interface Listener {
        fun onDoneClicked(task: Task, done: Boolean)
        fun onTaskClicked(task: Task)
        fun onTaskLongClicked(anchor: View, task: Task)
    }
}