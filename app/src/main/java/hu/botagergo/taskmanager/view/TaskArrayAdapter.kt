package hu.botagergo.taskmanager.view

import android.annotation.SuppressLint
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
import hu.botagergo.taskmanager.R
import hu.botagergo.taskmanager.group.Grouper
import hu.botagergo.taskmanager.log.logd
import hu.botagergo.taskmanager.model.Task
import hu.botagergo.taskmanager.task_filter.ConjugateTaskFilter
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

class TaskArrayAdapter(private var activity: Activity,
                       filter: ConjugateTaskFilter? = null, grouper: Grouper<Any, Task>? = null) :
    RecyclerView.Adapter<TaskArrayAdapter.ViewHolder>() {

    var tasks: ArrayList<Task>? = null
        set(value) {
            val newTasks = if (value != null) ArrayList<Task>().apply {
                this.addAll(value)
            } else null

            field = newTasks
            dataChanged = true

            //val diffResult =
            //    DiffUtil.calculateDiff(TaskDiffCallback(this.filteredTasks, newFilteredTasks))
            //diffResult.dispatchUpdatesTo(this)
        }


    var filter: ConjugateTaskFilter? = null
        set(value) {
            field = value
            dataChanged = true
        }

    var grouper: Grouper<Any, Task>? = null
        set(value) {
            field = value
            dataChanged = true
        }

    init {
        this.filter = filter
        this.grouper = grouper
    }

    interface Listener {
        fun onDoneClicked(task: Task, done: Boolean)
        fun onTaskClicked(task: Task)
        fun onTaskLongClicked(anchor: View, task: Task)
    }
    var listener: Listener? = null

    private var dataChanged = true

    private var displayTasksField: SortedMap<Any, List<Task>>? = null
    private val displayTasks: SortedMap<Any, List<Task>>
        get() {
            if (dataChanged) {
                logd(this, "dataChanged")
                val filteredTasks = ArrayList<Task>().apply {
                    if (tasks != null) {
                        this.addAll(tasks!!)
                        filter?.apply(this)
                    }
                }
                this.taskCount = filteredTasks.size
                this.showGroups = grouper != null
                this.displayTasksField = grouper?.group(filteredTasks) ?:
                        sortedMapOf({_, _ -> 0}, "" to filteredTasks)
                dataChanged = false
            }
            return displayTasksField!!
        }

    private var taskCount: Int = 0
    private var showGroups: Boolean = false

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        dataChanged = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTask = inflater.inflate(R.layout.item_task, parent, false)

        return ViewHolder(itemTask)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        logd(this, "onBindViewHolder")
        val (groupName, ind, task) = getTask(position)

        viewHolder.textView.text = task.title
        viewHolder.textViewStatus.text = task.status.value
        viewHolder.textViewContext.text =
            if (task.context != Task.Context.None) task.context.value else ""
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

        if (showGroups && groupName != null && ind == 0) {
            viewHolder.textViewGroup.visibility = View.VISIBLE
            viewHolder.textViewGroup.text = groupName.toString()
        } else {
            viewHolder.textViewGroup.visibility = View.GONE
        }

        val cardView = viewHolder.cardView
        val imageButton = viewHolder.imageButton

        cardView.setOnClickListener {
            val pos = viewHolder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onTaskClicked(task)
            }
        }

        cardView.setOnLongClickListener {
            val pos = viewHolder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onTaskLongClicked(it, task)
                true
            } else {
                false
            }
        }

        imageButton.setOnClickListener {
            logd(this, "imageButton.onClickListener")
            val pos = viewHolder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onDoneClicked(task, task.done)
            }
        }

    }

    private fun getDrawable(id: Int): Drawable? {
        return ResourcesCompat.getDrawable(activity.resources, id, null)
    }

    private fun getColor(id: Int): Color {
        return Color.valueOf(ResourcesCompat.getColor(activity.resources, id, null))
    }

    override fun getItemCount(): Int {
        this.displayTasks
        return taskCount
    }

    private fun getTask(position: Int): Triple<Any?, Int, Task> {
        var pos = position
        for (entry in displayTasks.entries) {
            if (pos < entry.value.size) {
                return Triple(entry.key, pos, entry.value[pos])
            } else {
                pos -= entry.value.size
            }
        }

        throw RuntimeException()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val textViewGroup: TextView = itemView.findViewById(R.id.textView_group)

        val textView: TextView = itemView.findViewById(R.id.textView_title)
        val textViewStatus: TextView = itemView.findViewById(R.id.textView_status)
        val textViewContext: TextView = itemView.findViewById(R.id.textView_context)
        val textViewComments: TextView = itemView.findViewById(R.id.textView_comments)
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
    }
}