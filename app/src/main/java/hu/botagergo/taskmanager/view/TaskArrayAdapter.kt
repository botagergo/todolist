package hu.botagergo.taskmanager.view

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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.taskmanager.R
import hu.botagergo.taskmanager.model.Task
import hu.botagergo.taskmanager.task_filter.ConjugateTaskFilter

class TaskArrayAdapter(private var activity: Activity) :
    RecyclerView.Adapter<TaskArrayAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView_title)
        val textViewStatus: TextView = itemView.findViewById(R.id.textView_status)
        val textViewContext: TextView = itemView.findViewById(R.id.textView_context)
        val textViewComments: TextView = itemView.findViewById(R.id.textView_comments)
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    private var tasks: ArrayList<Task> = ArrayList()
    private var filteredTasks: ArrayList<Task> = ArrayList()
    var filter: ConjugateTaskFilter = ConjugateTaskFilter()
        set(value) {
            field = value
            onFilterChanged()
        }

    var listener: Listener? = null

    class TaskDiffCallback(private val old: ArrayList<Task>, private val new: ArrayList<Task>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return old.size
        }

        override fun getNewListSize(): Int {
            return new.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].uid == new[newItemPosition].uid
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return old[oldItemPosition].equals(new[newItemPosition])
        }
    }

    fun getTasks(): ArrayList<Task> {
        return this.tasks
    }

    fun setTasks(tasks: ArrayList<Task>) {
        val newTasks = ArrayList<Task>().apply {
            this.addAll(tasks)
        }

        val newFilteredTasks = ArrayList<Task>().apply {
            this.addAll(newTasks)
            filter.apply(this)
        }

        val diffResult =
            DiffUtil.calculateDiff(TaskDiffCallback(this.filteredTasks, newFilteredTasks))

        this.tasks = newTasks
        this.filteredTasks = newFilteredTasks

        diffResult.dispatchUpdatesTo(this)
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
                listener?.onTaskClicked(filteredTasks[pos])
            }
        }

        cardView.setOnLongClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onTaskLongClicked(it, filteredTasks[pos])
                true
            } else {
                false
            }
        }

        imageButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onDoneClicked(filteredTasks[pos], !filteredTasks[pos].done)
            }
        }

        return holder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val task: Task = filteredTasks[position]

        viewHolder.textView.text = task.title
        viewHolder.textViewStatus.text = task.status.value
        viewHolder.textViewContext.text = if (task.context != Task.Context.None) task.context.value else ""
        viewHolder.textViewComments.text = task.comments

        val color = ResourcesCompat.getColor(
            viewHolder.textViewStatus.resources, when (task.status) {
                Task.Status.NextAction -> R.color.status_next_action
                Task.Status.Waiting -> R.color.status_waiting
                Task.Status.Planning -> R.color.status_planning
                Task.Status.OnHold -> R.color.status_on_hold
                Task.Status.None -> R.color.status_none
            }, null
        )

        viewHolder.textViewStatus.setTextColor(color)

        viewHolder.textViewComments.visibility =
            if (task.comments.isEmpty()) View.GONE else View.VISIBLE

        if (task.done) {
            viewHolder.cardView.background = getColor(R.color.task_done_background).toDrawable()
            viewHolder.imageButton.background = getColor(R.color.task_done_background).toDrawable()
            viewHolder.imageButton.setImageDrawable(getDrawable(R.drawable.ic_check_circle))
        } else {
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
        return filteredTasks.size
    }

    fun onFilterChanged() {
        val newFilteredTasks = ArrayList<Task>().apply {
            this.addAll(tasks)
            filter.apply(this)
        }

        val diffResult =
            DiffUtil.calculateDiff(TaskDiffCallback(this.filteredTasks, newFilteredTasks))

        this.filteredTasks = newFilteredTasks

        diffResult.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun onDoneClicked(task: Task, done: Boolean)
        fun onTaskClicked(task: Task)
        fun onTaskLongClicked(anchor: View, task: Task)
    }
}